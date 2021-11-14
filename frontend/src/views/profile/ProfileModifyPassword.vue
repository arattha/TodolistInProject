<template>
  <div class="flex flex-col justify-center items-center">
    <ValidationObserver class="w-full" ref="observer" v-slot="{ handleSubmit }">
      <form
        class="flex flex-col items-center justify-center w-full"
        @submit.prevent="handleSubmit(modifyPassword)"
      >
        <div
          class="
            flex flex-col
            justify-center
            items-center
            mt-16
            mb-10
            bg-itemGray
            rounded-lg
            w-11/12
            sm:w-7/12
            lg:w-5/12
            h-64
            shadow-md
          "
        >
          <ValidationProvider
            class="w-full flex flex-col justify-center items-center mb-10"
            name="비밀번호"
            vid="password"
            rules="required|min:8|max:30|alpha_num"
            v-slot="{ errors }"
          >
            <input
              type="password"
              autocomplete="off"
              class="
                w-7/12
                bg-contentGray
                flex-1
                appearance-none
                border border-transparent
                py-2
                px-4
                text-gray-700
                placeholder-gray-400
                shadow-md
                rounded-lg
                focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
              "
              placeholder="비밀번호"
              @keyup.enter="idKeyupEnter()"
              v-model="user.password"
            />

            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
          <ValidationProvider
            class="w-full flex flex-col justify-center items-center"
            name="비밀번호"
            rules="required|min:8|max:30|confirmed:password"
            v-slot="{ errors }"
          >
            <input
              type="password"
              autocomplete="off"
              class="
                w-7/12
                bg-contentGray
                flex-1
                appearance-none
                border border-transparent
                py-2
                px-4
                text-gray-700
                placeholder-gray-400
                shadow-md
                rounded-lg
                focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
              "
              placeholder="비밀번호 확인"
              @keyup.enter="idKeyupEnter()"
              v-model="user.passwordChk"
            />

            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
        </div>
        <button
          type="submit"
          class="
            bg-headerGray
            text-white text-base
            font-semibold
            w-20
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-menuGray
            focus:outline-none
            focus:ring-2
            focus:ring-blue-500
            focus:ring-offset-2
            focus:ring-offset-blue-200
          "
        >
          변경
        </button>
      </form>
    </ValidationObserver>
  </div>
</template>

<script>
import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate';
import { alpha_num, required, min, max, confirmed } from 'vee-validate/dist/rules';
import { changePassword } from '@/api/myPage.js';

setInteractionMode('eager');

extend('alpha_num', {
  ...alpha_num,
  message: '{_field_}는 숫자와 영어만 가능합니다.',
});

extend('required', {
  ...required,
  message: '{_field_}를 입력해주세요.',
});

extend('min', {
  ...min,
  message: '{_field_}는 {length}글자 이상 작성해야합니다.',
});

extend('max', {
  ...max,
  message: '{_field_}는 {length}글자 이하로 작성해야합니다.',
});

extend('confirmed', {
  ...confirmed,
  message: '{_field_}가 다릅니다.',
});

export default {
  name: 'Profile',
  components: {
    ValidationObserver,
    ValidationProvider,
  },
  created() {
    this.memberId = this.$route.params.memberId;
  },
  data() {
    return {
      memberId: '',
      user: {
        password: '',
        passwordChk: '',
      },
    };
  },
  methods: {
    modifyPassword() {
      changePassword(
        { id: this.memberId, password: this.user.password },
        (res) => {
          console.log(res);
          this.$router.push('/profile/' + this.memberId);
        },
        (error) => {
          console.error(error);
        }
      );
    },
  },
};
</script>
