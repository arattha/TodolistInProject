import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '../store';
import Home from '../views/Home.vue';
// import store from '@/store/index';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
  },
  {
    path: '/signup',
    name: 'Signup',
    component: () => import('@/views/Signup.vue'),
  },
  {
    path: '/alarm',
    name: 'Alarm',
    component: () => import('@/views/Alarm.vue'),
  },
  {
    path: '/projects',
    component: () => import('@/views/Project.vue'),
    children: [
      {
        path: '',
        name: 'TotalProject',
        component: () => import('@/views/project/TotalProject.vue'),
      },
      {
        path: 'done',
        name: 'DoneProject',
        component: () => import('@/views/project/DoneProject.vue'),
      },
    ],
  },
  {
    path: '/projects/:projectId/todos',
    component: () => import('@/views/Todo.vue'),
    children: [
      {
        path: '',
        name: 'TotalTodo',
        component: () => import('@/views/todo/TotalTodo.vue'),
      },
      {
        path: 'my',
        name: 'MyTodo',
        component: () => import('@/views/todo/MyTodo.vue'),
      },
      {
        path: 'progress',
        name: 'TodoProgress',
        component: () => import('@/views/todo/TodoProgress.vue'),
      },
      {
        path: '/:todoId/detail',
        component: () => import('@/views/TodoDetail.vue'),
        children: [
          {
            path: '',
            name: 'TodoContents',
            component: () => import('@/views/todoDetail/TodoContents.vue'),
          },
          {
            path: 'url',
            name: 'TodoURL',
            component: () => import('@/views/todoDetail/TodoURL.vue'),
          },
          {
            path: 'history',
            name: 'TodoHistory',
            component: () => import('@/views/todoDetail/TodoHistory.vue'),
          },
        ],
      },
    ],
  },

  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue'),
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  duplicateNavigationPolicy: 'ignore',
});

router.beforeEach((to, from, next) => {
  if (to.name === 'Project') {
    store.dispatch('set_project_name', '');
  }

  next();
});

export default router;
