import axios from 'axios';
import router from '@/router';
import store from '@/store/index';
import { reissuUser } from '@/api/auth.js';

export function setInterceptors(axiosService) {
  axiosService.interceptors.request.use(
    function (config) {
      // 요청을 보내기 전에 어떤 처리를 할 수 있다.
      return config;
    },
    function (error) {
      // 요청이 잘못되었을 때 에러가 컴포넌트 단으로 오기 전에 어떤 처리를 할 수 있다.
      return Promise.reject(error);
    }
  );

  axiosService.interceptors.response.use(
    function (response) {
      // 서버에 요청을 보내고 나서 응답을 받기 전에 어떤 처리를 할 수 있다.
      return response;
    },
    async function (error) {
      const errorApi = error.config;

      if (error.response.data.status === 401) {
        //if (!isTokenRefreshing) {
        errorApi.retry = true;
        // 재발급 요청
        let isReissu = false;

        await reissuUser(
          store.getters.id,
          (res) => {
            // 재발급 요청에 성공할 경우
            if (res.object) {
              isReissu = true;
            } else {
              // 재발급 요청에 실패했을 경우
              alert('로그인 유효기간이 만료되었습니다. \n다시 로그인해주세요.');
              store.dispatch('set_id', '');
              store.dispatch('set_nickname', '');
              store.dispatch('toggle_isLogin', false);
              this.$router.push('/login');
              //store.dispatch('set_type', 'logout');
              //store.dispatch('toggle_isLoading', true);
              //logout(store.getters.id);
            }
          },
          (error) => {
            alert('문제가 발생했습니다. 다시 시도해주세요.');
            console.log(error);
            store.dispatch('set_id', '');
            store.dispatch('set_nickname', '');
            store.dispatch('toggle_isLogin', false);
            router.push('/login');
          }
        );
        if (isReissu) {
          return await axios(errorApi);
        }
      } else {
        alert('문제가 발생했습니다. 다시 시도해주세요.');
        console.log(error);
        router.push('/login');
      }

      // 응답이 에러인 경우에 미리 전처리할 수 있다.
      return Promise.reject(error);
    }
  );

  return axiosService;
}
