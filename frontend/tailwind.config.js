module.exports = {
  purge: ['./src/**/*.{js,ts,jsx,tsx}'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      colors: {
        headerGray: '#4c5870',
        contentGray: '#EBEDEC',
        menuGray: '#A2A9Af',
        itemGray: '#DADADA',
        buttonGray: '#C4C4C4',
        progress: '#5AC4FF',
        add: '#FF5151',
        newStatus: 'C0C30A',
        done: '#AB84FF',
      },
      height: {
        contentHeight: 'calc(100% - 8rem)',
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
};
