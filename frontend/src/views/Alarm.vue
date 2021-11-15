<template>
  <div class="flex flex-col w-full h-full">
    <Header />
    <div class="flex my-5 mx-16 h-10">
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          w-20
          lg:w-24 lg:py-2 lg:px-2
          mr-8
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        @click="remove"
      >
        선택 삭제
      </button>
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          w-20
          lg:w-24 lg:py-2 lg:px-2
          mr-8
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        @click="removeAll"
      >
        모두 삭제
      </button>
    </div>

    <div
      class="
        flex flex-col
        items-center
        justify-center
        rounded-lg
        shadow-lg
        bg-itemGray
        overflow-y-auto
        mx-16
      "
    >
      <div v-if="alarmList" class="h-full overflow-y-auto scroll_type2 w-full">
        <div
          class="
            flex
            mx-auto
            justify-center
            items-center
            bg-contentGray
            w-11/12
            h-40
            md:h-24
            hover:bg-menuGray
            rounded-lg
            shadow-md
            md:my-3
            my-5
          "
          v-for="(alarm, index) in alarmList"
          :key="index"
        >
          <input class="flex w-10 h-5" type="checkbox" v-model="checkList" :value="alarm.id" />
          <div
            class="flex flex-col h-full w-full cursor-pointer border-itemGray border-l py-3 px-2"
            @click="goTodo(alarm.todoId)"
          >
            <div class="flex justify-center md:justify-start items-center h-full w-full">
              {{ alarm.content }}
            </div>
            <div class="flex justify-end items-center text-center text-xs">
              {{ alarm.regDate.split('T')[0] }}&nbsp;{{ alarm.regDate.split('T')[1] }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <Pagination
      class="flex justify-center items-center my-5"
      :pageCnt="pageCnt"
      :pageSize="pageSize"
      @paging="pagingMethod"
    />
  </div>
</template>

<script>
import { removeAllAlarm, removeAlarm, getAlarmInMypage } from '@/api/alarm.js';
import { mapActions, mapGetters } from 'vuex';
import Pagination from '@/components/Pagination';
import Header from '@/components/Header.vue';

export default {
  name: 'Alarm',
  components: {
    Header,
    Pagination,
  },
  props:['stomp'],
  data() {
    return {
      alarmList: [],
      checkList: [],
      pageCnt: 5,
      pageSize: 6,
    };
  },
  created() {
    this.pageCnt = 5;
    this.pageSize = 6;
    this.getAlarm();
  },
  computed: {
    ...mapGetters(['id', 'curPage', 'isDel', 'totalAlarmCnt']),
  },
  methods: {
    ...mapActions([
      'set_totalAlarmCnt',
      'set_curPage',
      'set_offset',
      'toggle_isDel',
      'set_todo_id',
    ]),
    async getAlarm() {
      let userData = {
        id: this.id,
        page: this.curPage - 1,
        size: 6,
        sort: 'regDate,desc',
      };

      await getAlarmInMypage(
        userData,
        (res) => {
          this.alarmList = res.object.content;

          if (!this.alarmList && this.isDel) {
            this.set_curPage(this.curPage - 1);
            this.getAlarm();
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    pagingMethod(page) {
      this.checkList = [];
      this.getAlarm(page);
    },
    remove() {
      if (this.checkList.length == 0) {
        alert('선택한 알람이 없습니다.');
        return;
      }

      removeAlarm(
        {
          memberId: this.id,
          checkList: this.checkList,
        },
        () => {
          this.toggle_isDel(true);
          this.set_totalAlarmCnt(this.totalAlarmCnt - this.checkList.length);
          this.getAlarm();
          this.checkList = [];
          alert('삭제되었습니다.');
          this.getRealtimeAlarm();
        },
        () => {
          return;
        }
      );
    },
    removeAll() {
      if (this.totalAlarmCnt == 0) {
        alert('알람이 없습니다.');
        return;
      }

      removeAllAlarm(
        this.id,
        () => {
          this.toggle_isDel(true);
          this.set_totalAlarmCnt(0);
          this.getAlarm();
          this.getRealtimeAlarm();
        },
        () => {}
      );
    },
    goTodo(todoId) {
      // 해당 Todo의 상세 페이지로 이동
      this.set_todo_id(todoId);
      this.$router.push('/' + todoId + '/detail');
    },
    getRealtimeAlarm() {
      this.stomp.send(
        '/server/getAlarm',
        JSON.stringify({
          memberId: this.id,
        }),
        {}
      );
    },
  },
  // 해당 페이지에서 나갈때 값을 초기화하도록 세팅
  // 만약 새로고침 시에는 url이 같으므로 변경 X
  beforeRouteLeave(to, from, next) {
    // just use `this` this.name = to.params.name next()
    if (to.fullPath !== from.fullPath) {
      this.set_curPage(1);
      this.set_offset(0);
    }

    next();
  },
};
</script>

<style></style>
