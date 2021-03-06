<template>
  <div class="flex flex-col h-full w-full overflow-auto scroll_type2">
    <ValidationObserver
      class="flex flex-col justify-center items-center w-full py-10"
      ref="observer"
      v-slot="{ handleSubmit }"
    >
      <div
        class="
          flex
          xl:flex-row
          flex-col
          items-center
          bg-itemGray
          rounded-lg
          w-11/12
          md:w-7/12
          py-5
          shadow-md
        "
      >
        <div class="flex items-center justify-center w-1/2 xl:py-5">
          <div
            class="
              flex
              relative
              justify-center
              items-center
              bg-contentGray
              rounded-full
              w-48
              h-48
              md:w-56 md:h-56
            "
          >
            <img :src="uploadImg" class="rounded-full flex object-cover w-full h-full" />

            <label
              for="getImgInput"
              class="
                flex
                justify-center
                items-center
                absolute
                rounded-full
                shadow-md
                border border-menuGray
                h-12
                w-12
                right-5
                bottom-0
                bg-buttonGray
                hover:bg-menuGray
                cursor-pointer
              "
            >
              <i class="fas fa-camera text-xl"></i>
            </label>
            <input
              type="file"
              id="getImgInput"
              class="hidden"
              name="getImgInput"
              accept="image/*"
              @change="imgUpload"
            />
          </div>
        </div>

        <div
          class="
            flex flex-col
            justify-center
            xl:items-start
            items-center
            w-11/12
            xl:w-1/2 xl:my-0
            mt-5
          "
        >
          <ValidationProvider
            name="이름"
            rules="required|min:2|max:30|korAlphaNum"
            v-slot="{ errors }"
            class="mb-5 xl:w-7/12 w-11/12"
          >
            <input
              class="
                bg-contentGray
                flex-1
                w-full
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
              placeholder="이름을 적어주세요."
              v-model="userModifyInfo.name"
              type="text"
            />
            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
          <!-- <div class="text-2xl mb-5">{{ userModifyInfo.team }}</div> -->
          <ValidationProvider
            class="mb-5 xl:w-7/12 w-11/12"
            name="이메일"
            rules="required|email|max:30"
            v-slot="{ errors }"
          >
            <input
              class="
                w-full
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
              placeholder="이메일을 적어주세요."
              v-model="userModifyInfo.email"
              type="text"
            />
            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
          <ValidationProvider
            class="xl:w-7/12 w-11/12 xl:mb-0 mb-5"
            name="전화번호"
            rules="required|min:5|max:20|numeric"
            v-slot="{ errors }"
          >
            <input
              class="
                w-full
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
              placeholder="전화번호를 적어주세요."
              v-model="userModifyInfo.phone"
              type="text"
            />
            <p class="text-red-500 font-black text-sm">{{ errors[0] }}</p>
          </ValidationProvider>
        </div>
      </div>
      <div class="flex justify-center items-center">
        <button
          class="
            flex
            justify-center
            items-center
            bg-headerGray
            text-white
            font-semibold
            w-20
            h-10
            mt-5
            py-2
            px-2
            rounded-lg
            shadow-md
            hover:bg-menuGray
            focus:outline-none
            focus:ring-2
            focus:ring-headerGray
            focus:ring-offset-2
            focus:ring-offset-purple-200
          "
          @click="handleSubmit(modifyProfile)"
        >
          수정
        </button>
      </div>
    </ValidationObserver>
  </div>
</template>

<script>
import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate';
import { numeric, email, required, min, max } from 'vee-validate/dist/rules';
import { getProfile, updateProfile } from '@/api/myPage.js';

setInteractionMode('eager');

extend('korAlphaNum', {
  validate: (value) => {
    let regex = /^[가-힣|aA-zZ| ]*$/.test(value);
    if (!regex) {
      return '올바른 한글, 영문만 입력해주세요.';
    } else {
      return true;
    }
  },
});

extend('numeric', {
  ...numeric,
  message: '{_field_}은(는) 숫자만 가능합니다.',
});

extend('required', {
  ...required,
  message: '{_field_}을(를) 입력해주세요.',
});

extend('min', {
  ...min,
  message: '{_field_}은(는) {length}글자 이상 작성해야합니다.',
});

extend('max', {
  ...max,
  message: '{_field_}은(는) {length}글자 이하로 작성해야합니다.',
});

extend('email', {
  ...email,
  message: '{_field_}을 잘못 입력하셨습니다.',
});

export default {
  name: 'Profile',
  components: {
    ValidationObserver,
    ValidationProvider,
  },
  created() {
    this.memberId = this.$route.params.memberId;
    this.uploadImg = 'http://localhost:8080/img/' + this.memberId;
    getProfile(
      this.memberId,
      (res) => {
        this.userInfo = JSON.parse(JSON.stringify(res.object));
        this.userModifyInfo = res.object;
      },
      (error) => {
        console.error(error);
      }
    );
  },
  data() {
    return {
      memberId: '',
      userInfo: {
        id: 1,
        name: '조성표',
        team: '프론트엔드팀',
        email: 'cspcsp@tip.com',
        phone: '01012341234',
      },
      userModifyInfo: {},
      img: '',
      uploadImg: '',
    };
  },
  methods: {
    imgUpload(e) {
      const inputImg = e.target.files;
      this.uploadImg = URL.createObjectURL(inputImg[0]);
      this.img = inputImg[0];
    },
    modifyProfile() {
      if (
        this.img === '' &&
        JSON.stringify(this.userInfo) === JSON.stringify(this.userModifyInfo)
      ) {
        alert('바뀐 내용이 없습니다!');
        return;
      }

      var formData = new FormData();
      formData.append('id', this.memberId);
      if (this.userInfo.email !== this.userModifyInfo.email) {
        formData.append('email', this.userModifyInfo.email);
      }
      if (this.userInfo.name !== this.userModifyInfo.name) {
        formData.append('name', this.userModifyInfo.name);
      }
      if (this.userInfo.phone !== this.userModifyInfo.phone) {
        formData.append('phone', this.userModifyInfo.phone);
      }
      if (this.img !== '') {
        formData.append('multipartFile', this.img);
      }

      updateProfile(
        formData,
        () => {
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
