const state = {
  bookmarkList: [],
};

const getters = {
  bookmarkList(state) {
    return state.bookmarkList;
  },
};

const mutations = {
  SET_BOOKMARK_LIST(state, payload) {
    state.bookmarkList = payload;
  },
  PUSH_BOOKMARK_LIST(state, payload) {
    state.bookmarkList.push(payload);
  },
  DELETE_BOOKMARK(state, payload) {
    const idx = state.bookmarkList.indexOf(payload);
    if(idx > -1) state.bookmarkList.splice(idx, 1);
  },
};

const actions = {
  set_bookmarkList(context, data) {
    context.commit('SET_BOOKMARK_LIST', data);
  },
  push_bookmarkList(context, data) {
    context.commit('PUSH_BOOKMARK_LIST', data);
  },
  delete_bookmark(context, data) {
    context.commit('DELETE_BOOKMARK', data);
  }
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
