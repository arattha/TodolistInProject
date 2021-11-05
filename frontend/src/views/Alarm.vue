<template>
  <div>
    <div v-for="(alarm, index) in alarmList" :key="index">
        <input type="checkbox" v-model="checkList" :value="alarm.id">{{alarm.content}}
      <button @click="goTodo(alarm.todoId)">이동</button>
    </div>
  </div>
</template>

<script>
import { removeAllAlarm, removeAlarm } from "@/api/alarm.js";
import { mapGetters } from "vuex";

export default {
  name: "Alarm",
  data() {
    return {
      alarmList: [],
      checkList: [],
    };
  },
  created() {
    this.alarmList = this.$route.params.alarmList;
  },
  computed: {
    ...mapGetters(["id"]),
  },
  methods: {
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

        for(var alarmId in this.checkList){
            removeAlarm(
              {
                memberId: this.id,
                alarmId: alarmId,
              },
              (res) => {
                this.alarmList = res.object;
      
                this.$route.params.alarmStomp.send(
                  "/server/checkAlarm",
                  JSON.stringify({
                    memberId: this.id,
                    alarmId: alarmId,
                  }),
                  {}
                );
              },
              () => {}
            );

        }

    },
    checkAll() {
      removeAllAlarm(
        {
          memberId: this.id,
        },
        () => {
          this.alarmList = [];

          this.$route.params.alarmStomp.send(
            "/server/checkAll",
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
