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
import HeaderPjtMenu from "@/components/HeaderPjtMenu.vue";
import Header from "@/components/Header.vue";
import ProjectCard from "@/components/ProjectCard.vue";
import { getProjectList } from "@/api/project.js";
import { formatDate } from "@/api/utils.js";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "TOTALPJT",
  components: {
    HeaderPjtMenu,
    Header,
    ProjectCard,
  },
  created() {
    let id = "0564293048818";
    getProjectList(
      true,
      id,
      (res) => {
        console.log(res);
        this.pjtInfoList = [];
        if (res.object) {
          for (let i = 0; i < res.object.length; i++) {
            let item = res.object[i];
            console.log(item["startDate"]);
            let temp = {};
            temp["pjt"] = {
              id: item["id"],
              name: item["name"],
              desc: item["desc"],
              startDate: formatDate(item["startDate"]),
              endDate: formatDate(item["endDate"]),
            };
            temp["totalCnt"] = item["totalCnt"];
            temp["progressCnt"] = item["progressCnt"];
            temp["doneCnt"] = item["doneCnt"];

            this.pjtInfoList.push(temp);
          }
        }
      },
      (error) => {
        alert("프로젝트 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!");
        console.log(error);
      }
    );
  },
  mounted() {},
  data() {
    return {
      ...mapGetters(["isLogin", "id", "nickname"]),
      pjtInfoList: [],
    };
  },
  methods: {},
};
</script>
