<template>
  <div>
    <Todo-Detail-Contents v-for="(detail, index) in detailList" :key="index" :detail="detail" />
  </div>
</template>

<script>
import TodoDetailContents from '@/components/TodoDetailContents.vue';
import { getTodoContent } from '@/api/todo.js';
import { formatDate } from '@/api/utils.js';

export default {
  name: 'TODOCONTENTS',
  components: {
    TodoDetailContents,
  },
  created() {
    this.todoId = this.$route.params.todoId;
  },
  watch: {
    todoId: {
      handler(id) {
        if (id) {
          getTodoContent(
            id,
            (res) => {
              this.detailList = [];
              for (let i = 0; i < res.object.length; i++) {
                let item = res.object[i];
                let temp = {
                  id: item['id'],
                  name: item['writer'],
                  content: item['contents'],
                  regDate: formatDate(item['regDate']),
                  profileImg: item['profileImg'],
                };
                this.detailList.push(temp);
              }
            },
            (error) => {
              alert('Todo 상세 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
              console.log(error);
            }
          );
        }
      },
      immediate: true,
    },
  },
  data() {
    return {
      detailList: [],
      todoId: '',
    };
  },
};
</script>
