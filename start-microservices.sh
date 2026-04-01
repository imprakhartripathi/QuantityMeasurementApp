#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${ROOT_DIR}/.env"
LOG_DIR="${ROOT_DIR}/logs"
COMPOSE_FILE="${ROOT_DIR}/docker-compose.yml"
MODE="${1:-local}"
DOCKER_COMPOSE_CMD=()

print_usage() {
  cat <<'EOF'
Usage:
  ./start-microservices.sh local          # JVM mode (gateway/auth/qma only)
  ./start-microservices.sh docker         # Docker mode (frontend + all services + postgres), build images
  ./start-microservices.sh docker-deploy  # Docker mode pull prebuilt images and run
  ./start-microservices.sh stop           # Stop docker stack
  ./start-microservices.sh clean          # Clean docker cached images (prune)
EOF
}

if [[ ! -f "${ENV_FILE}" ]]; then
  echo "Missing ${ENV_FILE}"
  echo "Copy .env.example to .env and fill in your values."
  exit 1
fi

load_env() {
  set -a
  # shellcheck disable=SC1090
  source "${ENV_FILE}"
  set +a
}

compose_cmd() {
  if [[ "${#DOCKER_COMPOSE_CMD[@]}" -eq 0 ]]; then
    if docker compose version >/dev/null 2>&1; then
      DOCKER_COMPOSE_CMD=("docker" "compose")
    elif command -v docker-compose >/dev/null 2>&1; then
      DOCKER_COMPOSE_CMD=("docker-compose")
    else
      echo "Docker Compose is not available. Install Docker Compose plugin or docker-compose binary."
      exit 1
    fi
  fi

  "${DOCKER_COMPOSE_CMD[@]}" --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" "$@"
}

mkdir -p "${LOG_DIR}"

declare -a PIDS=()
declare -a NAMES=()

require_non_empty() {
  local var_name="$1"
  local value="${!var_name:-}"
  if [[ -z "${value}" ]]; then
    echo "Missing required env var: ${var_name}"
    echo "Update ${ENV_FILE} and try again."
    exit 1
  fi
}

validate_env() {
  require_non_empty "QMA_JWT_SECRET"
}

validate_local_env() {
  export QMA_DB_URL="${QMA_DB_URL:-jdbc:postgresql://127.0.0.1:5432/quantity_measurement}"
  export QMA_DB_USERNAME="${QMA_DB_USERNAME:-}"
  export QMA_DB_PASSWORD="${QMA_DB_PASSWORD:-}"
  export QMA_JWT_SECRET="${QMA_JWT_SECRET:-change-me-to-a-long-random-secret-key-at-least-32-bytes}"
  export QMA_JWT_EXPIRATION_MS="${QMA_JWT_EXPIRATION_MS:-3600000}"
  export QMA_FRONTEND_ORIGIN="${QMA_FRONTEND_ORIGIN:-http://localhost:8000}"
  export QMA_OAUTH2_REDIRECT_URI="${QMA_OAUTH2_REDIRECT_URI:-http://localhost:8000/oauth/callback}"
  export GOOGLE_CLIENT_ID="${GOOGLE_CLIENT_ID:-}"
  export GOOGLE_CLIENT_SECRET="${GOOGLE_CLIENT_SECRET:-}"
  export QMA_AUTH_BASE_URL_LOCAL="${QMA_AUTH_BASE_URL_LOCAL:-http://localhost:5000}"
  export QMA_SERVICE_BASE_URL_LOCAL="${QMA_SERVICE_BASE_URL_LOCAL:-http://localhost:6000}"
  export QMA_AUTH_BASE_URL="${QMA_AUTH_BASE_URL_LOCAL}"
  export QMA_SERVICE_BASE_URL="${QMA_SERVICE_BASE_URL_LOCAL}"

  require_non_empty "QMA_DB_URL"
  require_non_empty "QMA_DB_USERNAME"
  require_non_empty "QMA_DB_PASSWORD"
}

validate_docker_deploy_env() {
  require_non_empty "QMA_API_IMAGE"
  require_non_empty "QMA_AUTH_IMAGE"
  require_non_empty "QMA_SERVICE_IMAGE"
  require_non_empty "QMA_CLIENT_IMAGE"
}

cleanup() {
  if [[ "${#PIDS[@]}" -eq 0 ]]; then
    return
  fi
  echo
  echo "Stopping services..."
  for pid in "${PIDS[@]}"; do
    kill "${pid}" >/dev/null 2>&1 || true
  done
  wait >/dev/null 2>&1 || true
  echo "All services stopped."
}

trap cleanup INT TERM EXIT

start_process() {
  local name="$1"
  local service_dir="$2"
  shift 2

  echo "Starting ${name}..."
  (
    cd "${ROOT_DIR}/${service_dir}"
    "$@"
  ) >"${LOG_DIR}/${name}.log" 2>&1 &

  local pid=$!
  PIDS+=("${pid}")
  NAMES+=("${name}")
  echo "  ${name} started (pid=${pid}) log=${LOG_DIR}/${name}.log"
}

process_alive() {
  local pid="$1"
  kill -0 "${pid}" >/dev/null 2>&1
}

process_name_by_pid() {
  local pid="$1"
  for i in "${!PIDS[@]}"; do
    if [[ "${PIDS[$i]}" == "${pid}" ]]; then
      echo "${NAMES[$i]}"
      return 0
    fi
  done
  echo "unknown-process"
}

startup_probe() {
  local probe_seconds=8
  local any_failed=0

  for ((sec = 1; sec <= probe_seconds; sec++)); do
    sleep 1
    for pid in "${PIDS[@]}"; do
      if ! process_alive "${pid}"; then
        any_failed=1
      fi
    done
    if [[ "${any_failed}" -eq 1 ]]; then
      break
    fi
  done

  if [[ "${any_failed}" -eq 1 ]]; then
    echo
    echo "Startup failed. At least one service exited early."
    for pid in "${PIDS[@]}"; do
      local name
      name="$(process_name_by_pid "${pid}")"
      if ! process_alive "${pid}"; then
        echo
        echo "----- ${name} (last 80 log lines) -----"
        tail -n 80 "${LOG_DIR}/${name}.log" || true
      fi
    done
    exit 1
  fi
}

wait_for_children() {
  while true; do
    set +e
    wait -n
    local exit_code=$?
    set -e

    local dead_pid=""
    for pid in "${PIDS[@]}"; do
      if ! process_alive "${pid}"; then
        dead_pid="${pid}"
        break
      fi
    done

    if [[ -n "${dead_pid}" ]]; then
      local dead_name
      dead_name="$(process_name_by_pid "${dead_pid}")"
      echo
      echo "Service exited: ${dead_name} (pid=${dead_pid}, code=${exit_code})"
      echo "----- ${dead_name} (last 80 log lines) -----"
      tail -n 80 "${LOG_DIR}/${dead_name}.log" || true
      exit "${exit_code}"
    fi
  done
}

run_local() {
  load_env
  validate_env
  validate_local_env

  start_process "qma-api-gateway" "QMA-API" ./mvnw spring-boot:run
  start_process "qma-auth" "QMA-Auth" ./mvnw spring-boot:run
  start_process "qma-service" "QMA-Service" ./mvnw spring-boot:run

  echo
  echo "Services launching..."
  echo "Gateway : http://localhost:4000"
  echo "Auth    : http://localhost:5000"
  echo "QMA     : http://localhost:6000"
  echo
  echo "Press Ctrl+C to stop all services."

  startup_probe
  wait_for_children
}

run_docker_build() {
  load_env
  validate_env
  compose_cmd up -d --build
  echo
  echo "Docker stack is up (built from source)."
  echo "Frontend: http://localhost:8000"
  echo "Gateway : http://localhost:4000"
}

run_docker_deploy() {
  load_env
  validate_env
  validate_docker_deploy_env
  compose_cmd pull
  compose_cmd up -d --no-build
  echo
  echo "Docker stack is up (pulled images)."
  echo "Frontend: http://localhost:8000"
  echo "Gateway : http://localhost:4000"
}

run_docker_stop() {
  compose_cmd down
  echo "Docker stack stopped."
}

run_docker_clean() {
  if ! command -v docker >/dev/null 2>&1; then
    echo "Docker is not installed or not in PATH."
    exit 1
  fi

  echo "Cleaning docker cached images..."
  docker image prune -a -f
  echo "Docker image cache cleaned."
}

case "${MODE}" in
  local)
    run_local
    ;;
  docker)
    run_docker_build
    ;;
  docker-deploy)
    run_docker_deploy
    ;;
  stop)
    run_docker_stop
    ;;
  clean)
    run_docker_clean
    ;;
  *)
    print_usage
    exit 1
    ;;
esac
