<template>
  <div class="flex flex-col h-full">
    <Header />
    <div class="flex flex-shrink-0 items-center bg-menuGray h-16">
      <div
        class="
          flex
          justify-center
          items-center
          text-lg
          font-black
          cursor-pointer
          h-full
          w-36
          mr-5
          ml-3
          hover:text-white
        "
        :class="{ 'text-white': pageType === 'myProfile' }"
        @click="goMyProfile()"
      >
        내 정보
      </div>
      <div
        class="
          flex
          justify-center
          items-center
          text-lg
          font-black
          cursor-pointer
          h-full
          w-36
          mr-5
          hover:text-white
        "
        :class="{ 'text-white': pageType === 'modifyProfile' }"
        @click="goMyProfileModify"
      >
        내 정보 변경
      </div>
      <div
        class="
          flex
          justify-center
          items-center
          text-lg
          font-black
          cursor-pointer
          h-full
          w-36
          mr-5
          hover:text-white
        "
        :class="{ 'text-white': pageType === 'modifyPwProfile' }"
        @click="goMyProfileModifyPassword"
      >
        비밀번호 변경
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';

export default {
  name: 'Profile',
  components: {
    Header,
  },
  created() {
    this.memberId = this.$route.params.memberId;

    if (this.$route.path === `/profile/${this.memberId}`) {
      this.pageType = 'myProfile';
    } else if (this.$route.path === `/profile/${this.memberId}/modify`) {
      this.pageType = 'modifyProfile';
    } else {
      this.pageType = 'modifyPwProfile';
    }

  },
  watch: {
    $route(to) {
      if (to.path === `/profile/${this.memberId}`) {
        this.pageType = 'myProfile';
      } else if (to.path === `/profile/${this.memberId}/modify`) {
        this.pageType = 'modifyProfile';
      } else {
        this.pageType = 'modifyPwProfile';
      }
    },
  },
  data() {
    return {
      memberId: '',
      pageType: '',
    };
  },
  methods: {
    goMyProfile() {
      if (this.$route.path !== `/profile/${this.memberId}`) {
        this.pageType = 'myProfile';
        this.$router.push(`/profile/${this.memberId}`);
      }
    },
    goMyProfileModify() {
      if (this.$route.path !== `/profile/${this.memberId}/modify`) {
        this.pageType = 'modifyProfile';
        this.$router.push(`/profile/${this.memberId}/modify`);
      }
    },
    goMyProfileModifyPassword() {
      if (this.$route.path !== `/profile/${this.memberId}/modify/pw`) {
        this.pageType = 'modifyPwProfile';
        this.$router.push(`/profile/${this.memberId}/modify/pw`);
      }
    },
  },
};
</script>
