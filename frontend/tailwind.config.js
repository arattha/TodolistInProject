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
      },
      height: {
        contentHeight: 'calc(100% - 4rem)',
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
};
