const state = {
  todoId: '1',
};

const getters = {
  todoId(state) {
    return state.todoId;
  },
};

const mutations = {
  SET_TODO_ID(state, payload) {
    state.todoId = payload;
  },
};

const actions = {
  set_todo_id(context, data) {
    context.commit('SET_TODO_ID', data);
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
