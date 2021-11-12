const state = {
  projectId: '',
  projectName: '',
  teamId: '',
};

const getters = {
  projectId(state) {
    return state.projectId;
  },
  projectName(state) {
    return state.projectName;
  },
  teamId(state) {
    return state.teamId;
  },
};

const mutations = {
  SET_PROJECT_ID(state, payload) {
    state.projectId = payload;
  },
  SET_PROJECT_NAME(state, payload) {
    state.projectName = payload;
  },
  SET_TEAM_ID(state, payload) {
    state.teamId = payload;
  },
};

const actions = {
  set_project_name(context, data) {
    context.commit('SET_PROJECT_NAME', data);
  },
  set_project_id(context, data) {
    context.commit('SET_PROJECT_ID', data);
  },
  set_team_id(context, data) {
    context.commit('SET_TEAM_ID', data);
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
