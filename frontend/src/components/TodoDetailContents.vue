<template>
  <div class="flex flex-col w-11/12 h-52 bg-contentGray rounded-lg my-3 px-8 py-3 shadow-lg">
    <div class="flex justify-between w-full">
      <div class="flex">
        <div class="flex items-end font-black text-2xl">{{ detail.name }}</div>
        <div class="flex ml-5 text-xs items-end pb-1">{{ detail.regDate }}</div>
      </div>
      <button
        class="
          my-auto
          bg-itemGray
          text-black text-sm
          font-semibold
          h-9
          w-20
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
        @click="showModal()"
      >
        수정
      </button>
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

export default {
  name: 'TODODETAILCONTENTS',
  props: ['detail'],
  components: {
    TodoDetailModifyModal,
  },
  data() {
    return {
      isShow: false,
      todoId: '',
    };
  },
  created() {
    this.todoId = this.$route.params.todoId;
  },
  methods: {
    ...mapActions(['toggle_reload_todo_detail']),
    showModal() {
      this.isShow = true;
    },
    closeModal() {
      this.toggle_reload_todo_detail(true);
      this.isShow = false;
    },
  },
  computed: {
    ...mapGetters(['reloadTodoDetail']),
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
