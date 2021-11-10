const state = {
    stomp: '',
  };
  
  const getters = {
    stomp(state) {
      return state.stomp;
    },
  };
  
  const mutations = {
    SET_STOMP(state, payload) {
      state.stomp = payload;
    },
  };
  
  const actions = {
    set_stomp(context, data) {
      context.commit('SET_STOMP', data);
    },
  };
  
  export default {
    strict: process.env.NODE_ENV !== 'production',
    state: {
      ...state,
    },
    getters,
    mutations,
    actions,
  };
  