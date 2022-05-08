import subprocess
import ctypes

with subprocess.Popen(['java', 'Main'], shell=False) as process: # must be shell=False
    process.wait()

handle = process._handle

creation_time = ctypes.c_ulonglong()
exit_time = ctypes.c_ulonglong()
kernel_time = ctypes.c_ulonglong()
user_time = ctypes.c_ulonglong()

rc = ctypes.windll.kernel32.GetProcessTimes(handle,
                                            ctypes.byref(creation_time),
                                            ctypes.byref(exit_time),
                                            ctypes.byref(kernel_time),
                                            ctypes.byref(user_time),
                                            )

print((exit_time.value - creation_time.value) / 10000000)
print((kernel_time.value + user_time.value) / 10000000)
