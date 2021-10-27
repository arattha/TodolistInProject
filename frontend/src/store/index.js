import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import header from './header';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    header,
  },
  plugins: [
    createPersistedState({
      paths: ['header'],
    }),
  ],
});
