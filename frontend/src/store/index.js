import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import header from './header';
import user from './user';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user,
    header,
  },
  plugins: [
    createPersistedState({
      paths: ['header','user'],
    }),
  ],
});
