<template>
  <div>
    <Header />
    <!-- <div v-for="(alarm, index) in alarmList" :key="index">
        <input type="checkbox" v-model="checkList" :value="index">{{alarm.content}}
      <button @click="goTodo(alarm.todoId)">이동</button>
    </div> -->

    <button @click="check">선택한 알림 삭제</button>
    <button @click="checkAll">모든 알림 삭제</button>
    
    <div v-if="alarmList">
      <div
        v-for="(alarm, index) in alarmList"
        :key="index"
      >
        <input type="checkbox" v-model="checkList" :value="index">
        <div class="
          flex flex-row
          h-14
          border-b-2 border-r-2 border-l-2
          cursor-pointer
          hover:bg-indigo-200
        ">
          <div
            class="
              hidden md:flex justify-center items-center p-2 h-full w-72 border-r-2 mr-2
            "
          >
            {{ alarm.content }}
          </div>
          <div class="flex justify-center items-center h-full w-32 text-center">
            {{ alarm.regDate.split('T')[0] }}<br />{{ alarm.regDate.split('T')[1] }}
          </div>
        </div>
      </div>
    </div>
    <Pagination class="mt-3" :pageCnt="pageCnt" :pageSize="pageSize" @paging="pagingMethod" />
    {{this.checkList}}
  </div>
</template>

<script>
import { removeAllAlarm, removeAlarm, getAlarmInMypage, getAlarmCnt } from "@/api/alarm.js";
import { mapActions, mapGetters } from "vuex";
import Pagination from '@/components/Pagination';
import Header from '@/components/Header.vue';

export default {
  name: "Alarm",
  components:{
    Header,
    Pagination
  },
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
    this.getTotalAlarmCnt();
    this.getAlarm();
  },
  computed: {
    ...mapGetters(['id', 'curPage', 'isDel', 'totalAlarmCnt']),
  },
  methods: {
    ...mapActions(['set_totalAlarmCnt', 'set_curPage', 'set_offset', 'toggle_isDel']),
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
    getTotalAlarmCnt() {
      getAlarmCnt(
        this.id,
        (res) => {
          this.set_totalAlarmCnt(res.object);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    pagingMethod(page) {
      console.log(this.alarmList);
      this.checkList = [];
      this.getAlarm(page);
    },
    selectAlarm(e){
        this.checkList = [];
        for(let i=0; i < e.options.length; i++) {
            const option = e.options[i];
            if(option.selected) {
                this.checkList.push(option.value);
            }
        }
    },
    check() {

      let removeAlarm = [];
      for(var i = 0 ; i < this.checkList.length ; i++){
        removeAlarm.push(this.alarmList[this.checkList[i]].id);
      }

      this.checkList = [];
      this.remove(removeAlarm);
      
    },
    remove(list){
      removeAlarm(
        {
          memberId: this.id,
          checkList: list
        },
        () => {

          this.toggle_isDel(true);
          this.set_totalAlarmCnt(this.totalAlarmCnt - list.length);
          this.getAlarm();
          alert("삭제되었습니다.");

          this.$route.params.alarmStomp.send(
            "/server/getAlarm",
            JSON.stringify({
              memberId: this.id,
            }),
            {}
          );
          
        },
        () => {
          return;
        }
      );
    },
    checkAll() {
      removeAllAlarm(
        this.id,
        () => {
          this.alarmList = [];

          this.$route.params.alarmStomp.send(
            "/server/getAlarm",
            JSON.stringify({
              memberId: this.id,
            }),
            {}
          );
        },
        () => {}
      );
    },
    goTodo(todoId) {
      // 해당 Todo의 상세 페이지로 이동
      // this.$route.push("/todoInfo");
      console.log(todoId);
    },
  },
};
</script>

<style></style>
