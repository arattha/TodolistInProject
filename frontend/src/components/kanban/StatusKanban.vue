<template>
  <div
    class="
      flex flex-col
      xl:flex-shrink
      flex-shrink-0
      pb-3
      mr-4
      bg-itemGray
      w-80
      xl:w-1/4
      rounded-md
      h-full
    "
  >
    <div class="flex flex-col px-5 py-2 mt-3 w-full">
      <div class="flex h-10 w-30 pb-2 text-xl font-black">
        {{ status }}
      </div>
      <div class="flex h-0 w-32 border-b-2 border-black"></div>
    </div>
    <div class="px-5 py-2">전체 할일 : {{ todoList.length }}</div>
    <div class="overflow-y-auto overflow-x-hidden scroll_type2 h-full">
      <draggable
        class="h-full"
        :list="todoList"
        group="todoGroup"
        v-bind="dragOptions"
        @start="setTodoId"
        @add="updateTeam($event, status)"
      >
        <div class="mb-6" v-for="(todoInfo, index) in todoFilter" :key="index + todoInfo.status">
          <Todo-Card :todoInfo="todoInfo" :key="index + todoInfo.bookmark" :stomp="stomp" />
        </div>
      </draggable>
    </div>
    <Todo-Team-Member-Move-Modal v-if="isShow" @justCloseModal="justCloseModal" @closeModal="closeModal" :todoInfo="todoInfoNew" :stomp="stomp" :selectedTeam="selectedTeam"/>
  </div>
</template>

<script>
import draggable from 'vuedraggable';
import TodoCard from '@/components/TodoCard.vue';
import { mapGetters } from 'vuex';
import TodoTeamMemberMoveModal from '@/components/modal/TodoTeamMemberMoveModal.vue';

export default {
  name: 'STATUSKANBAN',
  props: ['status', 'todoList', 'bookmarkFilter', 'stomp', 'selectedTeam'],
  data() {
    return {
      isShow: false,
      todoInfoNew: {},
    };
  },
  components: {
    TodoCard,
    draggable,
    TodoTeamMemberMoveModal
  },
  methods: {
    setTodoId(e) {
      this.todoId = this.todoList[e.oldIndex];
    },
    updateTeam(e, status) {
      if(this.todoList[e.newIndex].status == "New"){
        this.todoInfoNew = this.todoList[e.newIndex];
        this.isShow = true;

      } else if(status == "New"){

        this.stomp.send(
          '/server/moveTodo/status',
          JSON.stringify({
            id: this.todoList[e.newIndex].id,
            title: this.todoList[e.newIndex].title,
            status: status,
            projectId: this.todoList[e.newIndex].projectId,
            teamId: this.todoList[e.newIndex].teamId,
            teamName: this.todoList[e.newIndex].teamName,
            memberId: null,
            memberName: null,
            modifyDate: this.todoList[e.newIndex].modifyDate,
            regDate: this.todoList[e.newIndex].regDate,
          }),
          {}
        );

      } else {

        this.stomp.send(
          '/server/moveTodo/status',
          JSON.stringify({
            id: this.todoList[e.newIndex].id,
            title: this.todoList[e.newIndex].title,
            status: status,
            projectId: this.todoList[e.newIndex].projectId,
            teamId: this.todoList[e.newIndex].teamId,
            teamName: this.todoList[e.newIndex].teamName,
            memberId: this.todoList[e.newIndex].memberId,
            memberName: this.todoList[e.newIndex].memberName,
            modifyDate: this.todoList[e.newIndex].modifyDate,
            regDate: this.todoList[e.newIndex].regDate,
          }),
          {}
        );

        // 상태를 드래거블로 이동시 바꾸어주기 위한 메서드 MyTodo.vue 참고
        this.$emit('changeStatus', { index: e.newIndex, status: status });
      }

    },
    closeModal() {
      this.isShow = false;
    },
    justCloseModal(){
      this.isShow = false;
      this.stomp.send(
        '/server/getTeamTodo',
        JSON.stringify({
          projectId: this.projectId,
          teamId: this.todoList[0].teamId,
        }),
        {}
      );
    }
  },
  computed: {
    ...mapGetters(['projectId', 'id', 'projectName']),
    todoFilter: function () {
      let bookmarkFilter = this.bookmarkFilter;
      if (!bookmarkFilter) {
        return this.todoList; //filter가 없을 때는 원본 반환
      } else {
        return this.todoList.filter(function (todo) {
          if (todo.bookmark) {
            return true;
          }
        });
      }
    },
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
