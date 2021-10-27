<template>
  <div class="about">
    <Header />
    <h1>This is an about page</h1>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import { mapActions } from 'vuex';
export default {
  name: 'ABOUT',
  components: {
    Header,
  },
  created() {
    this.set_project_name('프로젝트 명');
  },
  methods: {
    ...mapActions(['set_project_name']),
  },
};
</script>
