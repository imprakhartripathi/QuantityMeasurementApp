#!/bin/sh
set -eu

api_base_url="${VITE_API_BASE_URL:-}"
escaped_api_base_url="$(printf '%s' "${api_base_url}" | sed 's/\\/\\\\/g; s/"/\\"/g')"

cat > /usr/share/nginx/html/runtime-config.js <<EOF
window.__QMA_CONFIG__ = {
  VITE_API_BASE_URL: "${escaped_api_base_url}",
}
EOF
