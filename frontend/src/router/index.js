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
    path: '/project',
    name: 'Project',
    component: () => import('@/views/TotalProject.vue'),
  },
  {
    path: '/todo',
    name: 'Todo',
    component: () => import('@/views/PjtTodo.vue'),
  },
  {
    path: '/detail',
    name: 'TodoDetail',
    component: () => import('@/views/TodoDetail.vue'),
    children: [
      {
        path: '/contents',
        name: 'TodoContents',
        component: () => import('@/views/TodoContents.vue'),
      },
      {
        path: '/url',
        name: 'TodoURL',
        component: () => import('@/views/TodoURL.vue'),
      },
      {
        path: '/history',
        name: 'TodoHistory',
        component: () => import('@/views/TodoHistory.vue'),
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
