<template>
  <div>
    <Todo-History-Card
      v-for="(history, index) in todoHistoryList"
      :key="index"
      :todoHistory="history"
    />
  </div>
</template>

<script>
import TodoHistoryCard from '@/components/TodoHistoryCard.vue';
import { getTodoRecords } from '@/api/todo.js';
import { formatDate } from '@/api/utils.js';
import { mapGetters } from 'vuex';
export default {
  name: 'TODOHISTORY',
  components: {
    TodoHistoryCard,
  },
  watch: {
    todoId: {
      handler(id) {
        if (id) {
          getTodoRecords(
            id,
            (res) => {
              for (let i = 0; i < res.object.length; i++) {
                let item = res.object[i];
                let temp = {
                  id: item['id'],
                  isContent: item['content'],
                  diff: item['diff'],
                  modifyDate: formatDate(item['modifyDate']),
                };
                this.todoHistoryList.push(temp);
              }
            },
            (error) => {
              if (this.isLogin) {
                alert('할일 히스토리 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
                console.log(error);
              }
            }
          );
        }
      },
      immediate: true,
    },
  },
  computed: {
    ...mapGetters(['todoId']),
  },
  data() {
    return {
      todoHistoryList: [],
    };
  },
};
</script>
