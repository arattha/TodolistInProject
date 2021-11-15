<template>
  <div
    class="
      flex
      justify-center
      items-center
      fixed
      top-0
      left-0
      right-0
      bottom-0
      w-screen
      h-screen
      z-100
      bg-buttonGray bg-opacity-75
    "
  >
    <div
      class="
        flex flex-col
        w-5/6
        sm:w-2/3
        h-5/6
        lg:w-5/12
        mx-auto
        rounded-md
        shadow-lg
        fixed
        translate-x-1/2 translate-y-1/2
        bg-white
        z-50
      "
      v-click-outside="onClickOutside"
    >
      <div class="flex flex-col h-full w-full">
        <div
          class="
            flex
            rounded-t-md
            bg-headerGray
            h-1/6
            w-full
            justify-center
            items-center
            text-white
            font-black
            text-3xl
          "
        >
          프로젝트 추가
        </div>
        <div class="flex flex-col h-full w-full bg-itemGray overflow-y-auto">
          <div class="flex flex-col justify-center items-center py-5">
            <div class="flex justify-between mb-3 w-10/12 lg:w-8/12">
              <div class="flex items-center font-black w-5/12 text-lg">프로젝트 명</div>
              <div class="flex flex-col w-7/12">
                <input
                  class="
                    bg-contentGray
                    flex-1
                    appearance-none
                    border border-transparent
                    py-2
                    px-4
                    text-gray-700
                    placeholder-gray-400
                    shadow-md
                    rounded-lg
                    focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
                  "
                  placeholder="프로젝트 명을 적어주세요."
                  v-model="projectName"
                  @input="typingProjectName"
                  type="text"
                />

                <p class="text-red-500 font-black text-xs mt-2" v-if="!projectNameValid">
                  프로젝트 명을 입력해주세요.
                </p>
              </div>
            </div>
            <div class="flex justify-between mb-3 w-10/12 lg:w-8/12">
              <div class="flex items-center font-black w-5/12 text-lg">시작일</div>
              <div class="flex flex-col w-7/12">
                <input
                  class="
                    bg-contentGray
                    flex-1
                    appearance-none
                    border border-transparent
                    py-2
                    px-4
                    text-gray-700
                    placeholder-gray-400
                    shadow-md
                    rounded-lg
                    focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
                  "
                  v-model="startDate"
                  @input="inputStartDate"
                  :min="minDate"
                  type="date"
                />

                <p class="text-red-500 font-black text-xs mt-2" v-if="!startDateValid">
                  시작일을 입력해주세요.
                </p>
              </div>
            </div>
            <div class="flex justify-between mb-3 w-10/12 lg:w-8/12">
              <div class="flex items-center font-black w-5/12 text-lg">종료일</div>
              <div class="flex flex-col w-7/12">
                <input
                  class="
                    bg-contentGray
                    flex-1
                    appearance-none
                    border border-transparent
                    py-2
                    px-4
                    text-gray-700
                    placeholder-gray-400
                    shadow-md
                    rounded-lg
                    focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
                  "
                  v-model="endDate"
                  @input="inputEndDate"
                  type="date"
                />

                <p class="text-red-500 font-black text-xs mt-2" v-if="!endDateValid">
                  종료일을 입력해주세요.
                </p>
              </div>
            </div>
          </div>
          <textarea
            class="
              overflow-y-auto
              scroll_type2
              bg-contentGray
              h-4/6
              rounded-md
              mx-8
              p-3
              resize-none
              shadow-lg
              appearance-none
              focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
            "
            v-model="inputContent"
            @input="checkByte"
            placeholder="내용을 입력해주세요."
          ></textarea>
          <div class="flex justify-end mx-8">
            <span :class="{ 'text-red-400': wordCnt > 500, 'font-black': wordCnt > 500 }">{{
              wordCnt
            }}</span>
            &nbsp;/ 500 Byte
          </div>
        </div>
        <div class="flex justify-center items-center p-5 bg-itemGray">
          <button
            class="
              bg-red-600
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-red-800
              focus:outline-none
              focus:ring-2
              focus:ring-red-500
              focus:ring-offset-2
              focus:ring-offset-red-200
              mr-10
            "
            @click="closeModal"
          >
            닫기
          </button>
          <button
            class="
              bg-headerGray
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-menuGray
              focus:outline-none
              focus:ring-2
              focus:ring-blue-500
              focus:ring-offset-2
              focus:ring-offset-blue-200
            "
            @click="addTodoDetail()"
          >
            추가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DOMPurify from 'dompurify';
import vClickOutside from 'v-click-outside';
import { mapGetters } from 'vuex';
import { addProject } from '@/api/project.js'

export default {
  name: 'PROJECTADDMODAL',
  data() {
    return {
      inputContent: '',
      wordCnt: 0,
      beforeByte: 0,
      projectName: '',
      startDate: '',
      endDate: '',
      projectNameValid: false,
      startDateValid: false,
      endDateValid: false,
      minDate: '',
    };
  },
  directives: {
    clickOutside: vClickOutside.directive,
  },
  created() {
    this.minDate = new Date().toISOString().substring(0, 10);
  },
  computed: {
    ...mapGetters(['id']),
  },
  methods: {
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      this.$emit('closeModal');
    },
    checkByte(e) {
      // const maxByte = 1000; //최대 1000바이트
      const text_len = e.target.value.length; //입력한 문자수

      let totalByte = 0;
      for (let i = 0; i < text_len; i++) {
        const each_char = e.target.value.charAt(i);
        const uni_char = escape(each_char); //유니코드 형식으로 변환
        if (uni_char.length > 4) {
          // 한글 : 2Byte
          totalByte += 2;
        } else {
          // 영문,숫자,특수문자 : 1Byte
          totalByte += 1;
        }
      }

      this.wordCnt = totalByte;
      this.inputContent = e.target.value;
    },
    addTodoDetail() {
      if (this.wordCnt > 500) {
        alert('최대 500Byte 까지 입력이 가능합니다.');
        return;
      }

      let changeContent = this.inputContent
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');

      //
      let changedText = DOMPurify.sanitize(changeContent);

      var data = {
        name: this.projectName,
        desc: changedText,
        isDone: false,
        startDate: this.startDate,
        endDate: this.endDate,
      }
      addProject(data, this.id,
      () => {
        alert('추가완료');
        this.closeModal();
      },
      () => {})

    },
    typingProjectName(e) {
      if (!e.target.value) {
        this.projectNameValid = false;
        return;
      }

      this.projectNameValid = true;
    },
    inputStartDate(e) {
      if (!e.target.value) {
        this.startDateValid = false;
        return;
      }

      if (e.target.value < this.minDate) {
        alert('현재 날짜보다 이전의 날짜는 선택할 수 없습니다.');
        e.target.value = this.minDate;
        this.startDate = this.minDate;
      }

      this.startDateValid = true;
    },
    inputEndDate(e) {
      if (!e.target.value) {
        this.endDateValid = false;
        return;
      }

      if (e.target.value < this.startDate) {
        alert('시작 날짜보다 이전의 날짜는 선택할 수 없습니다.');
        e.target.value = this.startDate;
        this.endDate = this.startDate;
      }

      this.endDateValid = true;
    },
  },
};
</script>
