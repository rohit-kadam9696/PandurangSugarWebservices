package com.twd.pandurangsugar.web.bean;

public class WebServerError {
	 private String msg;
	 private Integer error;
	 private boolean success;
	    public String getMsg() {
	        return msg;
	    }

	    public void setMsg(String msg) {
	        this.msg = msg;
	    }

		public Integer getError() {
			return error;
		}

		public void setError(Integer error) {
			this.error = error;
		}

		/**
		 * @return the success
		 */
		public boolean isSuccess() {
			return success;
		}

		/**
		 * @param success the success to set
		 */
		public void setSuccess(boolean success) {
			this.success = success;
		}

    
 }
