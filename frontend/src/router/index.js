import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '../store';
import { reissuUser } from '@/api/auth.js';
// import Home from '../views/Home.vue';
// import store from '@/store/index';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Login.vue'),
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
        path: 'team/:teamId',
        name: 'TeamTodo',
        component: () => import('@/views/todo/TeamTodo.vue'),
      },
      // {
      //   path: 'progress',
      //   name: 'TodoProgress',
      //   component: () => import('@/views/todo/TodoProgress.vue'),
      // },
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
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  duplicateNavigationPolicy: 'ignore',
});

router.beforeEach(async (to, from, next) => {
  console.log(to.name);
  if (to.name === 'Project') {
    store.dispatch('set_project_name', '');
    next();
  } else if (to.name !== 'Login' && to.name !== 'Signup') {
    if (store.getters.id) {
      await reissuUser(
        store.getters.id,
        (res) => {
          // 재발급 요청에 성공할 경우
          if (res.object) {
            if (to.name === 'Home') {
              next('/projects');
            } else {
              next();
            }
          } else {
            // 재발급 요청에 실패했을 경우
            next('/login');
          }
        },
        (error) => {
          alert('문제가 발생했습니다. 다시 시도해주세요.');
          console.log(error);
          // next('/login');
        }
      );
    } else {
      next('/login');
    }
  } else {
    next();
  }
});

export default router;
