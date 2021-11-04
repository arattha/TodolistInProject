<template>
  <div class="flex flex-col h-full">
    <Header />
    <Header-Pjt-Menu :type="'total'" />
    <div class="mx-16 my-5 font-black text-2xl border-b-2 border-black pb-2 w-1/3">Project</div>
    <div class="flex flex-col overflow-y-auto scroll_type2">
      <div class="px-16 w-full flex flex-col justify-center items-center">
        <Project-Card v-for="(pjtInfo, index) in pjtInfoList" :key="index" :pjtInfo="pjtInfo" />
      </div>
    </div>
  </div>
</template>

<script>
import HeaderPjtMenu from '@/components/HeaderPjtMenu.vue';
import Header from '@/components/Header.vue';
import ProjectCard from '@/components/ProjectCard.vue';

export default {
  name: 'TOTALPJT',
  components: {
    HeaderPjtMenu,
    Header,
    ProjectCard,
  },
  data() {
    return {
      pjtInfoList: [
        {
          pjt: {
            id: 1,
            name: 'OO프로젝트',
            startDate: '2021-10-10',
            endDate: '2021-11-11',
            desc: '이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다. 이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다. 이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다.',
          },
          totalCnt: 100,
          progressCnt: 200,
          doneCnt: 300,
        },

        {
          pjt: {
            id: 2,
            name: 'OO프로젝트',
            startDate: '2021-10-10',
            endDate: '2021-11-11',
            desc: '이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다.',
          },
          totalCnt: 100,
          progressCnt: 200,
          doneCnt: 300,
        },
        {
          pjt: {
            id: 3,
            name: 'OO프로젝트',
            startDate: '2021-10-10',
            endDate: '2021-11-11',
            desc: '이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다.',
          },
          totalCnt: 100,
          progressCnt: 200,
          doneCnt: 300,
        },
        {
          pjt: {
            id: 4,
            name: 'OO프로젝트',
            startDate: '2021-10-10',
            endDate: '2021-11-11',
            desc: '이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다.',
          },
          totalCnt: 100,
          progressCnt: 200,
          doneCnt: 300,
        },
        {
          pjt: {
            id: 5,
            name: 'OO프로젝트',
            startDate: '2021-10-10',
            endDate: '2021-11-11',
            desc: '이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다.',
          },
          totalCnt: 100,
          progressCnt: 200,
          doneCnt: 300,
        },
        {
          pjt: {
            id: 6,
            name: 'OO프로젝트',
            startDate: '2021-10-10',
            endDate: '2021-11-11',
            desc: '이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다.',
          },
          totalCnt: 100,
          progressCnt: 200,
          doneCnt: 300,
        },
        {
          pjt: {
            id: 7,
            name: 'OO프로젝트',
            startDate: '2021-10-10',
            endDate: '2021-11-11',
            desc: '이 프로젝트는 OO를 개발하며 XXX를 목표로 하는 프로젝트입니다.',
          },
          totalCnt: 100,
          progressCnt: 200,
          doneCnt: 300,
        },
      ],
    };
  },
};
</script>
