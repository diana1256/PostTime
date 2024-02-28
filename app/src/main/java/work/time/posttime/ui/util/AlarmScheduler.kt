package work.time.worktim.ui.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import work.time.adsert.ui.util.AlarmReceiver
import java.util.Calendar

class AlarmScheduler {

    fun scheduleAlarms(context: Context) {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
            scheduleAlarm(context, 8, 55, "Не забудьте отметить начало рабочего дня!")
            scheduleAlarm(context, 17, 55, "Не забудьте отметить конец рабочего дня!")
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleAlarm(context: Context, hour: Int, minute: Int, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("message", message)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            hour * 60 + minute,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Установите время для уведомления
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // Если время уже прошло, устанавливаем на следующий день
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Установка однократного будильника
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}