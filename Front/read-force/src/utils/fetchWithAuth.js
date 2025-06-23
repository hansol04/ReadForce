const fetchWithAuth = async (url, options = {}) => {
  const accessToken = localStorage.getItem('token');
  const refreshToken = localStorage.getItem('refresh_token');

  let res = await fetch(url, {
    ...options,
    headers: {
      ...(options.headers || {}),
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (res.status === 401 && refreshToken) {
    const refreshRes = await fetch(`/auth/reissue-refresh-token?refresh_token=${refreshToken}`, {
      method: 'POST',
    });

    if (refreshRes.ok) {
      const data = await refreshRes.json();
      localStorage.setItem('token', data.ACCESS_TOKEN);
      localStorage.setItem('refresh_token', data.REFRESH_TOKEN);

      // 🔁 재요청
      res = await fetch(url, {
        ...options,
        headers: {
          ...(options.headers || {}),
          Authorization: `Bearer ${data.ACCESS_TOKEN}`,
        },
      });
    } else {
      localStorage.clear();
      window.location.href = "/login";
    }
  }

  return res;
};

export default fetchWithAuth;
