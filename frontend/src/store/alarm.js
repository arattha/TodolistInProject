const state = {
    alarmStomp: '',
  };
  
  const getters = {
    alarmStomp(state) {
      return state.alarmStomp;
    },
  };
  
  const mutations = {
    SET_ALARMSTOMP(state, payload) {
      state.alarmStomp = payload;
    },
  };
  
  const actions = {
    set_alarmStomp(context, data) {
      context.commit('SET_ALARMSTOMP', data);
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
  