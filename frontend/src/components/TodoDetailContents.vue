<template>
  <div class="flex flex-col w-11/12 h-52 bg-contentGray rounded-lg my-3 px-8 py-3 shadow-lg">
    <div class="flex justify-between w-full">
      <div class="flex">
        <div class="rounded-full w-14 h-14 lg:w-16 lg:h-16 bg-white mr-3">
          <img :src="'http://localhost:8080/img/' + detail.writerId" />
        </div>
        <div class="flex items-end font-black text-2xl">{{ detail.name }}</div>
        <div class="flex ml-5 text-xs items-end pb-1">{{ detail.regDate }}</div>
      </div>
      <div class="flex relative">
        <button
          class="
            my-auto
            text-black text-sm
            font-semibold
            h-9
            w-10
            py-2
            px-2
            rounded-lg
            hover:bg-menuGray
          "
          :class="{ 'bg-menuGray': isShowMenu }"
          @click="toggleMenu"
        >
          <i class="fas fa-ellipsis-h"></i>
        </button>
        <div
          class="
            flex flex-col
            absolute
            top-9
            right-0
            h-16
            w-16
            bg-contentGray
            rounded-md
            shadow-md
            border
          "
          v-if="isShowMenu"
          v-click-outside="onClickOutside"
        >
          <div
            class="
              flex
              justify-center
              items-center
              h-1/2
              hover:bg-itemGray
              cursor-pointer
              text-sm
              font-bold
            "
            @click="showModal"
          >
            수정
          </div>
          <div
            class="
              flex
              justify-center
              items-center
              h-1/2
              hover:bg-itemGray
              cursor-pointer
              text-sm
              font-bold
            "
            @click="deleteTodoDetailContent"
          >
            삭제
          </div>
        </div>
      </div>
    </div>
    <div
      v-html="compiledMarkdown"
      class="todo-content h-52 overflow-y-auto mt-5 scroll_type2 break-all"
    ></div>
    <Todo-Detail-Modify-Modal
      v-if="isShow"
      @closeModal="closeModal"
      :todoId="todoId"
      :detailId="detail.id"
      :detailContent="detail.content"
    />
  </div>
</template>

<script>
import TodoDetailModifyModal from '@/components/modal/TodoDetailModifyModal';
import { mapGetters, mapActions } from 'vuex';
import { marked } from 'marked';
import DOMPurify from 'dompurify';
import vClickOutside from 'v-click-outside';
import { deleteTodoContent } from '@/api/todo.js';

export default {
  name: 'TODODETAILCONTENTS',
  props: ['detail'],
  components: {
    TodoDetailModifyModal,
  },
  data() {
    return {
      isShow: false,
      isShowMenu: false,
      todoId: '',
    };
  },
  directives: {
    clickOutside: vClickOutside.directive,
  },
  created() {
    this.todoId = this.$route.params.todoId;
  },
  methods: {
    ...mapActions(['toggle_reload_todo_detail']),
    showModal() {
      this.isShow = true;
      this.isShowMenu = false;
    },
    closeModal() {
      this.toggle_reload_todo_detail(true);
      this.isShow = false;
    },
    toggleMenu() {
      this.isShowMenu = !this.isShowMenu;
    },
    onClickOutside() {
      this.isShowMenu = false;
    },
    deleteTodoDetailContent() {
      this.isShowMenu = false;
      deleteTodoContent(
        this.detail.id,
        this.id,
        () => {
          this.toggle_reload_todo_detail(true);
        },
        (error) => {
          console.error(error);
          alert('삭제를 하는 도중에 오류가 발생했습니다.');
          this.toggle_reload_todo_detail(false);
        }
      );
    },
    // closeMenu() {
    //   this.isShowMenu = false;
    // },
  },
  computed: {
    ...mapGetters(['reloadTodoDetail', 'id']),
    compiledMarkdown: function () {
      marked.setOptions({
        renderer: new marked.Renderer(),
        gfm: true,
        headerIds: false,
        tables: true,
        breaks: true,
        pedantic: false,
        smartLists: true,
        smartypants: false,
      });

      let changeContent = this.detail.content
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');

      let changedText = DOMPurify.sanitize(marked(changeContent), { KEEP_CONTENT: false });

      // changedText = changedText.replace(regex, `<${tag} class=""`);
      return changedText;
    },
  },
};
</script>
