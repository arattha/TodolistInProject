<template>
  <div class="px-16 w-full h-full">
    <Project-Card v-for="(pjtInfo, index) in pjtInfoList" :key="index" :pjtInfo="pjtInfo" />
  </div>
</template>

<script>
import ProjectCard from '@/components/ProjectCard.vue';
import { getProjectList } from '@/api/project.js';
import { formatDate } from '@/api/utils.js';
import { mapGetters } from 'vuex';

export default {
  name: 'TOTALPROJECT',
  components: {
    ProjectCard,
  },
  data() {
    return {
      pjtInfoList: [],
    };
  },
  created() {
    this.pjtInfoList = [];
    this.getProject();
  },
  computed: {
    ...mapGetters(['id']),
  },
  methods: {
    getProject() {
      getProjectList(
        true,
        this.id,
        (res) => {
          this.pjtInfoList = [];
          if (res.object) {
            for (let i = 0; i < res.object.length; i++) {
              let item = res.object[i];

              let temp = {};
              temp['pjt'] = {
                id: item['id'],
                name: item['name'],
                desc: item['desc'],
                startDate: formatDate(item['startDate']),
                endDate: formatDate(item['endDate']),
              };
              temp['totalCnt'] = item['totalCnt'];
              temp['progressCnt'] = item['progressCnt'];
              temp['doneCnt'] = item['doneCnt'];

              this.pjtInfoList.push(temp);
            }
          }
        },
        (error) => {
          alert('프로젝트 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
          console.log(error);
        }
      );
    },
  },
};
</script>
