import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import header from './header';
import user from './user';
import todo from './todo';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user,
    header,
    todo,
  },
  plugins: [
    createPersistedState({
      paths: ['header', 'user', 'todo'],
    }),
  ],
});
