<template>
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
      cursor-pointer
      shadow-md
      hover:bg-opacity-40
    "
    @click="clicktodo"
  >
    <div class="flex justify-between items-center">
      <div class="flex items-center justify-center">
          <div class= "rounded-full w-10 h-10 mr-3 flex">
            <img class="rounded-full flex object-cover w-full h-full" :src="'http://localhost:8080/img/' + id" />
          </div>
        <div class="text-base font-bold">{{ todoInfo.memberName }}</div>
      </div>
      <div class="flex justify-center items-center">
        <div id="bookmark" class="mr-5 z-30">
          <i class="bookmark fas fa-star text-yellow-400" v-if="todoInfo.isBookmark"></i>
          <i class="bookmark fas fa-star text-white" v-else></i>
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
          z-30
        "
      >
        보내기
      </button>
    </div>
  </div>
</template>

<script>
import TodoStatus from '@/components/TodoStatus.vue';
import { addBookmark , deleteBookmark } from '@/api/bookmark.js';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'TODOCARD',
  data(){
    return {

    };
  },
  components: {
    TodoStatus,
  },
  props: ['todoInfo'],
  created(){
    
  },
  computed:{
    ...mapGetters(['id']),
  },
  methods: {
    ...mapActions(['set_todo_id']),
    todoSend() {
      console.log('보내기');
    },
    bookmark() {
      if(!this.todoInfo.isBookmark){
        addBookmark(
          {
            memberId : this.id, 
            todoId : this.todoInfo.id,
          },
          () => {
            this.todoInfo.isBookmark = true;
          },
          (error) => {
            alert('북마크 실패');
            console.log(error);
          }
        );
      } else {
        deleteBookmark(
          {
            memberId : this.id, 
            todoId : this.todoInfo.id,
          },
          () => {
            this.todoInfo.isBookmark = false;
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
        this.todoSend();
        return;
      }
      if (target == event.currentTarget.querySelector('.bookmark')) {
        this.bookmark();
        return;
      }
      console.log('이동 : ' + this.todoInfo.title + ' 상세 페이지');
      this.set_todo_id(this.todoInfo.id);
      this.$router.push(`/${this.todoInfo.id}/detail`);
    },
  },
};
</script>
