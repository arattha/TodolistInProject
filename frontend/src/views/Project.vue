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
    } else {
      this.pageType = 'done';
    }
  },
  watch: {
    $route(to) {
      if (to.path === '/projects') {
        this.pageType = 'total';
      } else if (to.path === '/projects/done') {
        this.pageType = 'done';
      }
    },
  },
  methods: {
    goTotalPjt() {
      if (this.pageType === 'total') {
        return;
      }
      this.$router.push('/projects');
    },
    goDonePjt() {
      if (this.pageType === 'done') {
        return;
      }
      this.$router.push('/projects/done');
    },
  },
};
</script>
