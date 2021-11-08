<template>
  <div
    class="
      flex flex-col
      lg:flex-row
      justify-between
      px-5
      pt-5
      pb-2
      lg:px-10 lg:py-2
      bg-itemGray
      h-56
      lg:h-36
      w-full
      rounded-lg
      shadow-lg
      cursor-pointer
      my-5
    "
    @click="goPjtTodo()"
  >
    <div class="flex justify-center flex-col w-full lg:w-10/12">
      <div class="flex lg:flex-row flex-col h-1/2">
        <div class="flex items-center font-black text-3xl">{{ pjtInfo.pjt.name }}</div>
        <div class="flex items-end text-xs my-3 lg:my-0 lg:mx-5 lg:pb-4">
          {{ pjtInfo.pjt.startDate }}~{{ pjtInfo.pjt.endDate }}
        </div>
      </div>
      <div class="flex items-center h-1/2 w-full mt-3 lg:mt-0 text-sm lg:text-base">
        {{ pjtInfo.pjt.desc }}
      </div>
    </div>
    <div class="flex lg:justify-center lg:items-center text-xs lg:text-sm lg:flex-col">
      <div class="mr-2 lg:mr-0 my-1">
        전체 할일 <span class="hidden lg:inline">&nbsp;&nbsp;</span> :
        <span class="font-black text-sm">{{ pjtInfo.totalCnt }}</span>
      </div>
      <div class="mr-2 lg:mr-0 my-1">
        진행중인 일 : <span class="font-black text-sm">{{ pjtInfo.progressCnt }}</span>
      </div>
      <div class="mr-2 lg:mr-0 my-1">
        완료한 일 <span class="hidden lg:inline">&nbsp;&nbsp;</span> :
        <span class="font-black text-sm">{{ pjtInfo.doneCnt }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  name: "PROJECTCARD",
  props: ["pjtInfo"],
  methods: {
    ...mapActions(["set_project_id", "set_project_name"]),
    goPjtTodo() {
      console.log(this.pjtInfo.pjt);
      this.set_project_id(this.pjtInfo.pjt.id);
      this.set_project_name(this.pjtInfo.pjt.name);
      this.$router.push(`/projects/${this.pjtInfo.pjt.id}/todos`);
    },
  },
};
</script>
