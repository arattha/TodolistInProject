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
  {
    path: '/profile/:memberId',
    component: () => import('@/views/Profile.vue'),
    children: [
      {
        path: '',
        name: 'Profile',
        component: () => import('@/views/profile/MyProfile.vue'),
      },
      {
        path: 'modify',
        name: 'ProfileModify',
        component: () => import('@/views/profile/ProfileModify.vue'),
      },
      {
        path: 'modify/pw',
        name: 'ProfileModifyPassword',
        component: () => import('@/views/profile/ProfileModifyPassword.vue'),
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
  if (to.path === '/projects' || to.path === '/projects/done') {
    store.dispatch('set_project_name', '');
  }

  if (to.name !== 'Login' && to.name !== 'Signup' && to.name !== 'Home') {
    if (store.getters.isLogin) {
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
            store.dispatch('toggle_isLoading', true);
            store.dispatch('set_id', '');
            store.dispatch('set_nickname', '');
            store.dispatch('toggle_isLogin', false);
            next('/login');
          }
        },
        (error) => {
          // alert('문제가 발생했습니다. 다시 시도해주세요.');
          console.log(error);
          store.dispatch('toggle_isLoading', true);
          store.dispatch('set_id', '');
          store.dispatch('set_nickname', '');
          store.dispatch('toggle_isLogin', false);
          next('/login');
        }
      );
    } else {
      store.dispatch('toggle_isLoading', true);
      store.dispatch('set_id', '');
      store.dispatch('set_nickname', '');
      store.dispatch('toggle_isLogin', false);
      next('/login');
    }
  } else {
    store.dispatch('toggle_isLoading', false);

    if (store.getters.isLogin) {
      next('/projects');
    } else {
      store.dispatch('set_id', '');
      store.dispatch('set_nickname', '');
      store.dispatch('toggle_isLogin', false);
      next();
    }
  }
});

export default router;
