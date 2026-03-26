/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#eefcfa',
          100: '#d2f7f2',
          200: '#a7eee6',
          500: '#14b8a6',
          700: '#0f766e',
          900: '#134e4a',
        },
      },
      boxShadow: {
        card: '0 12px 28px rgba(14, 26, 43, 0.08)',
      },
    },
  },
  plugins: [],
}
