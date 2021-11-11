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
        @click="goTotalTodo()"
      >
        전체 할일
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
        :class="{ 'text-white': pageType === 'my' }"
        @click="goMyTodo()"
      >
        내 할일
      </div>
      <!-- <div
        class="
          flex
          justify-center
          items-center
          text-lg
          font-black
          cursor-pointer
          h-full
          w-36
          hover:text-white
        "
        :class="{ 'text-white': pageType === 'progress' }"
        @click="goTodoProgress()"
      >
        진행 상황
      </div> -->
    </div>
    <router-view class="flex overflow-auto" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'TODO',
  components: {
    Header,
  },
  data() {
    return {
      pageType: '',
    };
  },
  created() {
    this.pageType = 'total';
  },
  computed: {
    ...mapGetters(['projectId']),
  },
  methods: {
    ...mapActions(['set_project_name', 'set_project_id']),

    goTotalTodo() {
      this.pageType = 'total';
      this.$router.push('/projects/' + this.projectId + '/todos');
    },
    goMyTodo() {
      this.pageType = 'my';
      this.$router.push('/projects/' + this.projectId + '/todos/my');
    },
    goTodoProgress() {
      this.pageType = 'progress';
      this.$router.push('/projects/' + this.projectId + '/progress');
    },
  },
};
</script>
