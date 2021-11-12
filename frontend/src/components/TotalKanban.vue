<template>
  <div class="flex w-96">
    <div class="flex flex-col bg-itemGray rounded-lg shadow-lg w-full h-full">
      <div class="flex justify-between items-center px-5 py-2 my-3">
        <div
          class="flex border-b-2 w-24 pb-2 border-black text-xl font-black cursor-pointer"
          @click="goTeamTodo"
        >
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
          @click="showModal()"
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
          <div class="mb-6" v-for="(todoInfo, index) in todoFilter" :key="index">
            <Todo-Card :todoInfo="todoInfo" />
          </div>
        </draggable>
      </div>
    </div>
    <Todo-Add-Modal v-if="isShow" :teamId="teamInfo.teamId" @closeModal="closeModal" />
  </div>
</template>

<script>
import TodoCard from '@/components/TodoCard.vue';
import TodoAddModal from '@/components/modal/TodoAddModal.vue';
import draggable from 'vuedraggable';
import { mapGetters } from 'vuex';
export default {
  name: 'MYTODOADDMODAL',
  components: {
    TodoCard,
    TodoAddModal,
    draggable,
  },
  props: ['teamInfo', 'filters', 'bookmarkFilter'],
  data() {
    return {
      isShow: false,
      drag: false,
      teamId: '',
      todoId: '',
    };
  },
  created() {
    this.teamId = this.teamInfo.teamId;
  },
  methods: {
    todoAdd() {
      this.showModal();
    },

    setTodoId(e) {
      // console.log("setTodoId :", e.oldIndex);
      this.todoId = this.teamInfo.todoInfoList[e.oldIndex];
    },
    updateTeam(e) {
      this.stomp.send(
        '/server/moveTodo/team',
        JSON.stringify({
          id: this.teamInfo.todoInfoList[e.newIndex].id,
          title: this.teamInfo.todoInfoList[e.newIndex].title,
          status: this.teamInfo.todoInfoList[e.newIndex].status,
          projectId: this.teamInfo.todoInfoList[e.newIndex].projectId,
          teamId: this.teamInfo.teamId,
          teamName: this.teamInfo.teamName,
          memberId: this.teamInfo.todoInfoList[e.newIndex].memberId,
          memberName: this.teamInfo.todoInfoList[e.newIndex].memberName,
          modifyDate: this.teamInfo.todoInfoList[e.newIndex].modifyDate,
          regDate: this.teamInfo.todoInfoList[e.newIndex].regDate,
        }),
        {}
      );
    },
    showModal() {
      this.isShow = true;
    },
    closeModal() {
      this.isShow = false;
    },
    goTeamTodo() {
      const projectId = this.$route.params.projectId;
      this.$router.push(`/projects/${projectId}/todos/team/${this.teamId}`);
    },
  },
  computed: {
    ...mapGetters(['projectId', 'id', 'projectName', 'stomp']),
    todoFilter: function () {
      let filters = this.filters;
      let bookmarkFilter = this.bookmarkFilter;
      if (!bookmarkFilter) {
        if (filters == null) {
          return this.teamInfo.todoInfoList; //filter가 없을 때는 원본 반환
        } else {
          return this.teamInfo.todoInfoList.filter(function (todo) {
            if (filters.status.indexOf(todo.status) > -1) {
              return true;
            }
          });
        }
      } else {
        let bookmarkedTodoList = [];
        this.teamInfo.todoInfoList.forEach((todo) => {
          if (todo.isBookmark) bookmarkedTodoList.push(todo);
        });
        if (filters == null) {
          return bookmarkedTodoList;
        } else {
          return bookmarkedTodoList.filter(function (todo) {
            if (filters.status.indexOf(todo.status) > -1) {
              return true;
            }
          });
        }
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
  watch: {
    teamInfo: {
      immediate: true,
      handler(teamInfo) {
        teamInfo.totalCnt = teamInfo.todoInfoList.length;
        teamInfo.addCnt = 0;
        teamInfo.doneCnt = 0;
        teamInfo.progressCnt = 0;

        teamInfo.todoInfoList.forEach((todo) => {
          if (todo.status === '접수') {
            teamInfo.addCnt++;
          } else if (todo.status === '진행') {
            teamInfo.progressCnt++;
          } else if (todo.status === '완료') {
            teamInfo.doneCnt++;
          }
        });
      },
    },
  },
};
</script>
