const state = {
  projectName: 'test',
};

const getters = {
  projectName(state) {
    return state.projectName;
  },
  // get_id: (state) => state.id,
};

const mutations = {
  SET_PROJECT_NAME(state, payload) {
    state.projectName = payload;
  },
};

const actions = {
  set_project_name(context, data) {
    context.commit('SET_PROJECT_NAME', data);
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
