<template>
  <div class="px-16 w-full h-full">
    <button
      class="
        bg-itemGray
        text-black text-xs
        font-semibold
        w-16
        h-8
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
      @click="showModal"
    >
      추가하기
    </button>
    <Project-Card
      v-for="(pjtInfo, index) in pjtInfoList"
      :key="index"
      :pjtInfo="pjtInfo"
      @deleteCard="deleteCard"
      @closeModal="closeModal"
    />
    <Project-Add-Modal v-if="isShow" @closeModal="closeModal" />
  </div>
</template>

<script>
import ProjectCard from '@/components/ProjectCard.vue';
import ProjectAddModal from '@/components/modal/ProjectAddModal.vue';
import { getProjectList } from '@/api/project.js';
import { formatDate } from '@/api/utils.js';
import { mapGetters } from 'vuex';

export default {
  name: 'TOTALPROJECT',
  components: {
    ProjectCard,
    ProjectAddModal,
  },
  data() {
    return {
      pjtInfoList: [],
      isShow: false,
    };
  },
  created() {
    this.pjtInfoList = [];
    this.getProject();
  },
  computed: {
    ...mapGetters(['id', 'isLogin']),
  },
  methods: {
    getProject() {
      getProjectList(
        false,
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
          if (this.isLogin) {
            alert('프로젝트 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
            console.log(error);
          }
        }
      );
    },
    deleteCard() {
      this.getProject();
    },
    showModal() {
      this.isShow = true;
    },
    closeModal() {
      this.isShow = false;
      this.getProject();
    },
  },
};
</script>
