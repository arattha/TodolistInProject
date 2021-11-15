<template>
  <div>
    <div
      id="todoCard"
      class="
        mx-auto
        bg-contentGray
        grid
        px-4
        w-11/12
        h-32
        rounded-lg
        shadow-md
        cursor-pointer
        hover:bg-opacity-40
        z-0
      "
      @click="clicktodo"
    >
      <div class="flex justify-between items-center">
        <div class="flex items-center justify-center">
          <div class="rounded-full w-10 h-10 mr-3 flex">
            <img
              class="rounded-full flex object-cover w-full h-full"
              :src="'http://localhost:8080/img/' + todoInfo.memberId"
            />
          </div>
          <div class="text-base font-bold">{{ todoInfo.memberName }}</div>
        </div>
        <div class="flex justify-center items-center">
          <div id="bookmark" class="mr-5 z-30">
            <i
              class="bookmark fas fa-star"
              :class="{ 'text-yellow-400': todoInfo.bookmark, 'text-white': !todoInfo.bookmark }"
            ></i>
          </div>
          <Todo-Status :status="todoInfo.status" :isDetail="false" />
        </div>
      </div>
      <div class="flex justify-between items-center">
        <div class="text-lg font-medium">
          {{ todoInfo.title }}
        </div>
        <button
          class="
            sendBtn
            flex
            w-16
            h-8
            justify-center
            items-center
            bg-buttonGray
            text-black text-xs
            font-semibold
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
            z-40
          "
        >
          보내기
        </button>
      </div>
    </div>
    <Todo-Team-Member-Move-Modal v-if="isShow" @closeModal="closeModal" :todoInfo="todoInfo" :stomp="stomp"/>
  </div>
</template>

<script>
import TodoStatus from '@/components/TodoStatus.vue';
import TodoTeamMemberMoveModal from '@/components/modal/TodoTeamMemberMoveModal.vue';
import { addBookmark, deleteBookmark } from '@/api/bookmark.js';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'TODOCARD',
  data() {
    return {
      isShow: false,
    };
  },
  components: {
    TodoStatus,
    TodoTeamMemberMoveModal,
  },
  props: ['todoInfo', 'stomp'],
  computed: {
    ...mapGetters(['id', 'bookmarkList']),
  },
  methods: {
    ...mapActions(['set_todo_id', 'push_bookmarkList', 'delete_bookmark']),
    bookmark() {
      if (!this.todoInfo.bookmark) {
        addBookmark(
          {
            memberId: this.id,
            todoId: this.todoInfo.id,
          },
          () => {
            this.push_bookmarkList(this.todoInfo.id);
            this.todoInfo.bookmark = true;
          },
          (error) => {
            alert('북마크 실패');
            console.log(error);
          }
        );
      } else {
        deleteBookmark(
          {
            memberId: this.id,
            todoId: this.todoInfo.id,
          },
          () => {
            this.delete_bookmark(this.todoInfo.id);
            this.todoInfo.bookmark = false;
          },
          (error) => {
            alert('북마크 실패');
            console.log(error);
          }
        );
      }
    },
    clicktodo(event) {
      let target = event.target;
      if (target == event.currentTarget.querySelector('.sendBtn')) {
        this.showModal();
        return;
      }
      if (target == event.currentTarget.querySelector('.bookmark')) {
        this.bookmark();
        return;
      }

      this.set_todo_id(this.todoInfo.id);
      this.$router.push(`/${this.todoInfo.id}/detail`);
    },
    showModal() {
      this.isShow = true;
    },
    closeModal() {
      this.isShow = false;
    },
  },
};
</script>
