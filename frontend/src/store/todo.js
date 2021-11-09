const state = {
  todoId: '1',
  reloadTodoDetail: false,
};

const getters = {
  todoId(state) {
    return state.todoId;
  },
  reloadTodoDetail(state) {
    return state.reloadTodoDetail;
  },
};

const mutations = {
  SET_TODO_ID(state, payload) {
    state.todoId = payload;
  },
  TOGGLE_RELOAD_TODO_DETAIL(state, payload) {
    state.reloadTodoDetail = payload;
  },
};

const actions = {
  set_todo_id(context, data) {
    context.commit('SET_TODO_ID', data);
  },
  toggle_reload_todo_detail(context, data) {
    context.commit('TOGGLE_RELOAD_TODO_DETAIL', data);
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
