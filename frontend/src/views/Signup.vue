<template>
  <div class="flex flex-col items-center h-full">
    <div class="flex h-96 w-auto justify-center items-center">
      <img src="@/images/logoBig.png" class="h-2/4 w-auto" />
    </div>
    <div
      class="
        flex flex-col
        justify-center
        items-center
        w-11/12
        md:w-2/3
        xl:w-1/3
        border-2 border-gray-300
        rounded-md
        p-2
      "
    >
      <ValidationObserver class="w-full" ref="observer" v-slot="{ handleSubmit }">
        <form
          class="flex flex-col items-center justify-center w-full"
          @submit.prevent="handleSubmit(signup)"
        >
          <ValidationProvider
            class="w-full flex flex-col justify-center items-center"
            name="아이디"
            rules="required|min:4|max:15|alpha_num|idCheck"
            v-slot="{ errors }"
          >
            <input
              type="text"
              autocomplete="off"
              class="
                appearance-none
                border border-transparent
                h-11
                w-11/12
                my-3
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
              v-model="user.id"
            />
            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
          <ValidationProvider
            class="w-full flex flex-col justify-center items-center"
            name="비밀번호"
            vid="password"
            rules="required|min:8|max:30|alpha_num"
            v-slot="{ errors }"
          >
            <input
              type="password"
              autocomplete="off"
              class="
                appearance-none
                border border-transparent
                h-11
                w-11/12
                my-3
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
                appearance-none
                border border-transparent
                h-11
                w-11/12
                my-3
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
              placeholder="비밀번호 확인"
              @keyup.enter="idKeyupEnter()"
              v-model="user.passwordChk"
            />

            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
          <ValidationProvider
            class="w-full flex flex-col justify-center items-center"
            name="이메일"
            rules="required|email|max:30"
            v-slot="{ errors }"
          >
            <input
              type="text"
              class="
                appearance-none
                border border-transparent
                h-11
                w-11/12
                my-3
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
              placeholder="이메일"
              @keyup.enter="idKeyupEnter()"
              v-model="user.email"
            />

            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
          <ValidationProvider
            class="w-full flex flex-col justify-center items-center"
            name="전화번호"
            rules="required|min:5|max:20|numeric"
            v-slot="{ errors }"
          >
            <input
              type="text"
              class="
                appearance-none
                border border-transparent
                h-11
                w-11/12
                my-3
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
              placeholder="전화번호 (숫자만)"
              @keyup.enter="idKeyupEnter()"
              v-model="user.phone"
            />

            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
          <button
            type="submit"
            class="
              w-11/12
              h-11
              my-3
              bg-headerGray
              text-white text-base
              font-semibold
              py-2
              px-4
              rounded-lg
              shadow-md
              focus:outline-none
              focus:ring-2
              focus:ring-headerGray
              focus:ring-offset-2
              focus:ring-offset-purple-200
            "
          >
            회원가입
          </button>
        </form>
      </ValidationObserver>
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
      @click="goBack()"
    >
      뒤로가기
    </div>
  </div>
</template>

<script>
import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate';
import { alpha_num, numeric, email, required, min, max, confirmed } from 'vee-validate/dist/rules';

setInteractionMode('eager');

extend('alpha_num', {
  ...alpha_num,
  message: '{_field_}는(은) 숫자와 영어만 가능합니다.',
});

extend('numeric', {
  ...numeric,
  message: '{_field_}는(은) 숫자만 가능합니다.',
});

extend('required', {
  ...required,
  message: '{_field_}를(을) 입력해주세요.',
});

extend('min', {
  ...min,
  message: '{_field_}는(은) {length}글자 이상 작성해야합니다.',
});

extend('max', {
  ...max,
  message: '{_field_}는(은) {length}글자 이하로 작성해야합니다.',
});

extend('email', {
  ...email,
  message: '{_field_}을 잘못 입력하셨습니다.',
});

extend('confirmed', {
  ...confirmed,
  message: '{_field_}가 다릅니다.',
});

extend('idCheck', {
  validate: async function (value) {
    // 해당 flag를 실제 사용시 false로 바꿔주세요
    let flag = true;

    // 서버요청을 통해 아이디가 있는지를 검사
    // 로직 추가 필요
    // value에 사용자가 입력한 아이디가 넘어오므로
    // value를 사용해서 아이디 중복을 검사
    // 예제 (예제 대로 여기에 코드 넣지말고 api폴더로 빼서 임포트해서 쓸것!)
    /*
    await axios
      .get(http://localhost:8078/user/search/${value})
      .then((response) => {
        if (response.data == 'ok') {
          flag = true;
        }
      })
      .catch((error) => {
        console.dir(error);
        return '서버 오류가 발생했습니다.';
      });
    */
    console.log(value);

    if (flag) {
      return true;
    } else {
      return '이미 사용중인 ID 입니다.';
    }
  },
});

export default {
  name: 'SIGNUP',
  components: {
    ValidationObserver,
    ValidationProvider,
  },
  data() {
    return {
      user: {
        id: '',
        password: '',
        passwordChk: '',
        email: '',
        phone: '',
      },
    };
  },
  methods: {
    goBack() {
      this.$router.push('/login');
    },
    signup() {
      console.log('회원가입');
    },
  },
};
</script>
