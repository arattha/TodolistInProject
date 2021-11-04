<template>
  <div class="flex w-96">
    <div class="flex flex-col bg-itemGray rounded-lg shadow-lg w-full h-full">
      <div class="flex justify-between items-center px-5 py-2 my-3">
        <div class="flex border-b-2 w-24 pb-2 border-black text-xl font-black">
          {{ teamInfo.teamName }}
        </div>
        <button
          class="
            flex
            justify-center
            items-center
            bg-contentGray
            text-black text-sm
            font-semibold
            h-10
            w-24
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-menuGray
            focus:outline-none
            focus:ring-2
            focus:ring-headerGray
            focus:ring-offset-2
            focus:ring-offset-purple-200
          "
          @click="todoAdd()"
        >
          할일 추가
        </button>
      </div>
      <div
        class="
          flex flex-col
          focus-within:items-center
          justify-start
          text-xs
          w-full
          font-semibold
          px-5
          my-3
        "
      >
        <div class="flex flex-row w-full mb-2">
          <div class="w-1/2">전체 할일 : {{ teamInfo.totalCnt }}</div>
          <div class="w-1/2">접수한 일 : {{ teamInfo.addCnt }}</div>
        </div>
        <div class="flex flex-row w-full">
          <div class="w-1/2">완료한 일 : {{ teamInfo.doneCnt }}</div>
          <div class="w-1/2">진행중인 일 : {{ teamInfo.progressCnt }}</div>
        </div>
      </div>
      <div class="overflow-y-auto overflow-x-hidden scroll_type2 h-full">
        <draggable
          class="h-full"
          :list="teamInfo.todoInfoList"
          group="todoGroup"
          v-bind="dragOptions"
          @start="setTodoId"
          @add="updateTeam"
        >
          <div class="mb-6" v-for="(todoInfo, index) in teamInfo.todoInfoList" :key="index">
            <Todo-Card :todoInfo="todoInfo" />
          </div>
        </draggable>
      </div>
    </div>
  </div>
</template>

<script>
import TodoCard from '@/components/TodoCard.vue';
import draggable from 'vuedraggable';
export default {
  name: 'TOTALKANBAN',
  components: {
    TodoCard,
    draggable,
  },
  props: ['teamInfo','TodoStomp'],
  data() {
    return {
      drag: false,
      teamId: "",
      todoId: "",
    };
  },
  created(){
    this.teamId = this.teamInfo.teamId;
  },
  methods: {
    todoAdd() {
      console.log('할일 추가');
    },
    // draggable 관련 로그를 찍기위한 함수
    // draggable 컴포넌트에 @change="log"를 추가해서 사용
    // log(e) {
    //   console.log(e);
    //   console.log(this.teamInfo.teamName, this.teamInfo.todoInfoList);
    // },
    setTodoId(e){
      // console.log("setTodoId :", e.oldIndex);
      this.todoId = this.teamInfo.todoInfoList[e.oldIndex];
    },
    updateTeam(e){
      
      this.TodoStomp.send(
            "/server/moveTodo/team",
            JSON.stringify({
              id:this.teamInfo.todoInfoList[e.newIndex].id,
              title:this.teamInfo.todoInfoList[e.newIndex].title,
              status:this.teamInfo.todoInfoList[e.newIndex].status,
              projectId:this.teamInfo.todoInfoList[e.newIndex].projectId,
              teamId:this.teamId,
              memberId:this.teamInfo.todoInfoList[e.newIndex].memberId,
              memberName:this.teamInfo.todoInfoList[e.newIndex].memberName,
              modifyDate:this.teamInfo.todoInfoList[e.newIndex].modifyDate,
              regDate:this.teamInfo.todoInfoList[e.newIndex].regDate
            }),
            {}
          );
      
    }
  },
  computed: {
    dragOptions() {
      return {
        animation: 200,
        group: 'todoGroup',
        disabled: false,
        ghostClass: 'ghost',
      };
    },
  },
};
</script>
