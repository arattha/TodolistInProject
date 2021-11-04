const state = {
  projectId: "",
  projectName: "",
};

const getters = {
  projectId(state) {
    return state.projectId;
  },
  projectName(state) {
    return state.projectName;
  },
  // get_id: (state) => state.id,
};

const mutations = {
  SET_PROJECT_ID(state, payload) {
    state.projectId = payload;
  },
  SET_PROJECT_NAME(state, payload) {
    state.projectName = payload;
  },
};

const actions = {
  set_project_name(context, data) {
    context.commit("SET_PROJECT_NAME", data);
  },
  set_project_id(context, data) {
    context.commit("SET_PROJECT_ID", data);
  },
};

export default {
  strict: process.env.NODE_ENV !== "production",
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
};
