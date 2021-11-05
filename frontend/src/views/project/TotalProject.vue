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
  created() {
    let id = '0564293048818';
    this.pjtInfoList = [];
    getProjectList(
      true,
      id,
      (res) => {
        console.log(res);
        this.pjtInfoList = [];
        if (res.object) {
          for (let i = 0; i < res.object.length; i++) {
            let item = res.object[i];
            console.log(item['startDate']);
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
  data() {
    return {
      ...mapGetters(['isLogin', 'id', 'nickname']),
      pjtInfoList: [],
    };
  },
};
</script>
