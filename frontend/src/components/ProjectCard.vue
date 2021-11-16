<template>
<div>
  <div
    class="
      flex flex-col
      justify-between
      px-5
      pt-5
      pb-2
      lg:px-10 lg:py-2
      bg-itemGray
      w-full
      rounded-lg
      shadow-lg
      cursor-pointer
      hover:bg-menuGray
      my-5
    "
    @click="goPjtTodo"
  >
    <div class="flex flex-col lg:flex-row">
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
        <div class="flex items-center w-full py-3">
          <button
            class="
              modifyBtn
              bg-buttonGray
              text-black text-xs
              font-semibold
              w-16
              h-8
              py-2
              px-2
              mr-8
              mt-4
              rounded-lg
              shadow-md
              hover:bg-itemGray
              focus:outline-none
              focus:ring-2
              focus:ring-headerGray
              focus:ring-offset-2
              focus:ring-offset-purple-200
            "
          >
            수정
          </button>
          <button
            class="
              doneBtn
              bg-buttonGray
              text-black text-xs
              font-semibold
              w-16
              h-8
              py-2
              px-2
              mr-8
              mt-4
              rounded-lg
              shadow-md
              hover:bg-itemGray
              focus:outline-none
              focus:ring-2
              focus:ring-headerGray
              focus:ring-offset-2
              focus:ring-offset-purple-200
            "
          >
            완료
          </button>
        </div>
      </div>
      <div class="flex lg:justify-center lg:items-center text-xs lg:text-sm lg:flex-col">
        <div class="flex mr-2 lg:mr-0 my-1">
          전체 할일 <span class="hidden lg:flex">&nbsp;&nbsp;</span> :
          <span class="flex font-black text-sm">{{ pjtInfo.totalCnt }}</span>
        </div>
        <div class="flex mr-2 lg:mr-0 my-1">
          진행중인 일 : <span class="flex font-black text-sm">{{ pjtInfo.progressCnt }}</span>
        </div>
        <div class="flex mr-2 lg:mr-0 my-1">
          완료한 일 <span class="hidden lg:flex">&nbsp;&nbsp;</span> :
          <span class="flex font-black text-sm">{{ pjtInfo.doneCnt }}</span>
        </div>
      </div>
    </div>
    
  </div>
  <Project-Add-Modal v-if="isModalShow" :pjt="pjtInfo.pjt" @closeModal="closeModal"/>
</div>
</template>

<script>
import { mapActions } from 'vuex';
import { changeDoneProject } from '@/api/project.js';
import ProjectAddModal from '@/components/modal/ProjectAddModal.vue';
export default {
  name: 'PROJECTCARD',
  props: ['pjtInfo'],
  components: {
    ProjectAddModal
  },
  data(){
    return{
      isModalShow:false,
    }
  },
  methods: {
    ...mapActions(['set_project_id', 'set_project_name']),
    showModal() {
      this.isModalShow = true;
    },
    closeModal() {
      this.$emit('closeModal');
      this.isModalShow = false;
    },
    goPjtTodo(event) {
      let target = event.target;
      if (target == event.currentTarget.querySelector('.modifyBtn')) {
        this.showModal();
        return;
      } 
      if (target == event.currentTarget.querySelector('.doneBtn')) {
        this.doneProject();
        return;
      }
      this.set_project_id(this.pjtInfo.pjt.id);
      this.set_project_name(this.pjtInfo.pjt.name);
      this.$router.push(`/projects/${this.pjtInfo.pjt.id}/todos`);
    },
    doneProject() {
      if (!confirm(this.pjtInfo.pjt.name + ' 프로젝트를 삭제/복구 하시겠습니까?')) {
        return;
      }
      changeDoneProject(
        this.pjtInfo.pjt.id,
        () => {
          this.$emit('deleteCard');
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
};
</script>
