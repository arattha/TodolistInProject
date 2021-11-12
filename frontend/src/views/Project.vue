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
        :class="{ 'text-white': pageType === 'total' }"
        @click="goTotalPjt()"
      >
        진행 프로젝트
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
        :class="{ 'text-white': pageType === 'done' }"
        @click="goDonePjt()"
      >
        완료 프로젝트
      </div>
    </div>
    <div class="flex mx-16 my-5 font-black text-2xl border-b-2 border-black pb-2 w-1/3">
      Project
    </div>
    <router-view class="flex flex-col h-full overflow-y-auto scroll_type2" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'TOTALPJT',
  components: {
    Header,
  },
  data() {
    return {
      ...mapGetters(['isLogin', 'id', 'nickname']),
      pageType: '',
    };
  },
  created() {
    if (this.$route.path === '/projects') {
      this.pageType = 'total';
    }
  },
  methods: {
    goTotalPjt() {
      this.pageType = 'total';
      this.$router.push('/projects');
    },
    goDonePjt() {
      this.pageType = 'done';
      this.$router.push('/projects/done');
    },
    addPjt() {
      // console.log('프로젝트 추가');
    },
  },
};
</script>
