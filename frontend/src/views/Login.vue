<template>
  <div class="grid justify-items-center items-center">
    <div class="grid grid-rows-2 justify-items-center items-center h-3/4">
      <div class="flex h-full w-full justify-center items-center">
        <img src="@/images/login.png" class="h-3/4 w-auto" />
      </div>
      <div class="grid grid-rows-3 h-full w-10/12 md:w-2/3 border-2 border-gray-300 rounded-md p-2">
        <div class="grid items-center justify-items-center">
          <input
            type="text"
            class="
              appearance-none
              border border-transparent
              h-2/3
              w-11/12
              py-2
              px-4
              bg-itemGray
              text-gray-700
              placeholder-gray-500
              shadow-md
              rounded-sm
              text-base
              focus:outline-none focus:ring-2 focus:ring-menuGray focus:border-transparent
            "
            placeholder="아이디"
            @keyup.enter="idKeyupEnter()"
          />
        </div>
        <div class="grid items-center justify-items-center">
          <input
            type="text"
            class="
              appearance-none
              border border-transparent
              w-11/12
              h-2/3
              py-2
              px-4
              bg-itemGray
              text-gray-700
              placeholder-gray-500
              shadow-md
              rounded-sm
              text-base
              focus:outline-none focus:ring-2 focus:ring-menuGray focus:border-transparent
            "
            placeholder="비밀번호"
            @keyup.enter="pwKeyupEnter()"
          />
        </div>
        <div class="grid items-center justify-items-center">
          <button
            class="
              w-11/12
              h-2/3
              bg-headerGray
              text-white text-base
              font-semibold
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-menuGray
              focus:outline-none
              focus:ring-2
              focus:ring-headerGray
              focus:ring-offset-2
              focus:ring-offset-purple-200
            "
            @click="login()"
            @keyup.enter="loginKeyupEnter()"
          >
            로그인
          </button>
        </div>
      </div>
      <div
        class="
          w-20
          grid
          items-center
          justify-items-center
          text-gray-500
          mt-5
          hover:opacity-50
          cursor-pointer
        "
        @click="goSignup()"
      >
        회원가입
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  methods: {
    login() {
      alert('로그인 로직!');
    },
    goSignup() {
      this.$router.push('/signup');
    },
    idKeyupEnter() {
      alert('id에서 엔터!');
    },
    pwKeyupEnter() {
      alert('pw에서 엔터!');
    },
    loginKeyupEnter() {
      alert('login버튼 에서 엔터!');
    },
  },
};
</script>
