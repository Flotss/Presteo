/** @type {import('tailwindcss').Config} */
export default {
  content: [],
  theme: {
    extend: {
      keyframes: {
        wrapIn: {
          'from': { maxHeight: '0', opacity: '0' },
          'to': { maxHeight: '500px', opacity: '1' }
        },
        wrapOut: {
          'from': { maxHeight: '500px', opacity: '1' },
          'to': { maxHeight: '0', opacity: '0' }
        }
      },
      animation: {
        'wrapIn': 'wrapIn 0.4s ease-in-out',
        'wrapOut': 'wrapOut 0.4s ease-in-out'
      }
    }
  },
  plugins: [],
}

