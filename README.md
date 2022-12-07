# spring-cloud-oauth2.0-demo
Spring Security OAuth2.0认证授权
一、登录认证的概述：
  1.登录认证：用户身份认证即用户去访问系统资源时系统要求验证用户的身份信息，身份合法方可继续访问
              常见的用户身份认证表现形式有：用户名密码登录，指纹打卡等方式。说通俗点，就相当于校验用户账号密码是否正确。
  2.用户授权：用户认证通过后去访问系统的资源，系统会判断用户是否拥有访问资源的权限，只允许访问有权限的系统资源，
              没有权限的资源将无法访问，这个过程叫用户授权。
  3.认证方式：
    单点登录认证：一次认证，多个系统进行使用。我们需要实现让用户在一个系统中登录（认证服务），其他任意受信任的系统都可以访问
    第三方认证-授权码模式：认证请求-授权码-第三方进行认证-返回认证之后的令牌-访问系统-认证令牌的合法性
二、认证的技术实现方案
  1.单点登录技术方案--使用存储媒介--不推荐使用
    1.1 分布式系统要实现单点登录，通常将认证系统独立抽取出来（认证微服务），并且将用户身份信息存储在单独的存储介质，
	    比如：MySQL、Redis，考虑性能要求，通常存储在Redis中
	1.2 优缺点：
	  1、认证系统为独立的系统。 
      2、各子系统通过Http或其它协议与认证系统通信，完成用户认证。 
      3、用户身份信息存储在Redis集群。
	1.3 单点登录常用的技术框架：
	  1、Apache Shiro. 
      2、CAS 
      3、Spring security CAS  
  2.Oauth2.0认证
    2.1 概述：OAuth2.0属于一个开放的授权认证标准，
	    允许用户授权第三方移动应用(外部系统)访问自己(例如：微信信息-服务提供者)服务器上的资源,用户无需向第三方应用（外部系统）提供账号或者密码。
	2.2 认证模式：
        2.2.1 用户密码模式--系统自己存在用户信息系统
        2.2.2 授权码模式--系统请求 授权服务系统 获取用户信息，完成授权
		      授权码模式流程：
			    角色：用户、第三方系统（客户端）、授权系统（认证服务-微信、QQ、支付宝）、用户信息
				用户访问第三方系统-选择授权模式-第三方系统向授权系统发送认证请求-授权系统向用户提供授权页面
				-用户选择授权确认-访问授权系统-授权系统返回授权码给第三方系统-第三方系统拿到授权码请求授权系统申请令牌-
				授权信息进行认证-返回授权令牌给第三方系统-第三方系统拿到令牌访问授权系统的用户信息-用户信息认证令牌的合法性
				-返回用户信息-第三方信息展示用户信息-登录认证完成
  3.Spring security Oauth2认证解决方案
    3.1 Oauth2能完成用户认证和用户授权
	3.2 认证授权过程：
	  1、用户请求认证服务完成认证。 
      2、认证服务下发用户身份令牌，拥有身份令牌表示身份合法。 
      3、用户携带令牌请求资源服务，请求资源服务必先经过网关。     
      4、网关校验用户身份令牌的合法，不合法表示用户没有登录，如果合法则放行继续访问。     
      5、资源服务获取令牌，根据令牌完成授权--权限控制。 
      6、资源服务完成授权则响应资源信息。
  4. SpringSecurity Oauth2.0入门
    4.1 授权码模式
	  4.1.1 申请授权码
	    请求认证服务，获取授权码
	      Get请求：--发起认证请求，请求认证服务，返回授权页面
          http://localhost:9001/oauth/authorize?client_id=changgou&response_type=code&scop=app&redirect_uri=http://localhost
		  参数详解：
		    client_id：客户端id，和授权配置类中设置的客户端id一致。 
            response_type：授权码模式固定为code 
            scop：客户端范围，和授权配置类中设置的scop一致。 
            redirect_uri：跳转uri，当授权码申请成功后会跳转到此地址，并在后边带上code参数（授权码）
		客户端跳转到登录页面,输入用户名和密码--客户端id和秘钥--与认证服务AuthorizationServerConfig客户端配置类型配置的一致--发送认证请求
	    点击登录，进入授权页面：	
		点击授权，认证服务（授权系统）携带授权码跳转redirect_uri,code=k45iLY就是返回的授权码
	  4.1.2 申请令牌
	    拿到授权码后，客户端发送请求向认证服务申请令牌。 Post请求：http://localhost:9001/oauth/token 
		  参数如下： 
	        grant_type：授权类型，填写authorization_code，表示授权码模式 
            code：授权码，就是刚刚获取的授权码，注意：授权码只使用一次就无效了，需要重新申请。 
            redirect_uri：申请授权码时的跳转url，一定和申请授权码时用的redirect_uri一致。
	      点击发送获取到令牌
		4.2.3 令牌校验
		  Get: http://localhost:9001/oauth/check_token?token= [access_token] --认证服务
		4.2.4 刷新令牌：
		 Post：http://localhost:9001/oauth/token 
		 参数如下：
		   grant_type： 固定为 refresh_token 
		   refresh_token：刷新令牌（注意不是access_token，而是refresh_token）
		   
		大致步骤：
		  1.申请授权码  http://localhost:9001/oauth/authorize?client_id=changgou&response_type=code&scop=app&redirect_uri=http://localhost
          2.登录界面输入客户端ID和客户端秘钥--授权服务指定的用户名称和秘钥 
          3.点击authorize授权获取授权码  
          4.用户授权码获取令牌  http://localhost:9001/oauth/token
	4.2 密码模式：
	  密码模式和授权码模式不一样之处在于不需要发送请求获取授权码，再根据授权码获取token，而是直接通过用户和密码发送请求获取token
	  4.2.1 发送认证请求获取token
	    Post请求：http://localhost:9001/oauth/token 
        参数： 
          grant_type：密码模式授权填写password 
          username：账号 -用户账号
          password：密码 -用户密码          
          即便用密码授权，客户端ID和客户端秘钥也必须要传到后台认证。
	  4.2.2 校验令牌：
	    Get: http://localhost:9001/oauth/check_token?token=[token] 
		参数： 
           token：令牌 
      4.2.3 刷新令牌：
         Post：http://localhost:9001/oauth/token 	  
         参数：    
            grant_type： 固定为 refresh_token 
            refresh_token：刷新令牌（注意不是access_token，而是refresh_token）    
	 大致步骤：
	   1.客户端ID和客户端秘钥需要传递到后台
       2.用户账号和密码
  5.资源服务器的登录检验--使用公钥校验
      登录认证之后生成的token使用私钥生成令牌
	  访问资源服务器的时候使用公钥对私钥进行解密，判断token的合法性
  6.认证服务的开发

  7.oauth2.0包括两个服务：授权服务（认证服务）和资源服务--oath提供的 
    1.授权服务：包含对接入端（客户端）以及登录用户的合法性进行检验并办法token功能，对令牌的请求端（客户端）有SpringMVC实现，
	  oauth/authorize：授权码模式，客户端访问授权服务器，向认证服务器发起认证请求。检验客户端的合法性，检验成功，认证服务器会返回授权页面
	  /oauth/token :客户端向认证服务器申请令牌的url
	2.资源服务器：包含对资源的保护功能，对非法请求的拦截、对请求中的token进行检验，使用如下过滤器实现对资源服务器的保护
	  Oauth2AuthentionProcessingFilter用来对请求给出的token解析鉴权
	3.oauth2.0 常用端点：
      /oauth/authorize ：授权端点-发起认证请求，检验客户端合法性，进行授权
      /oauth/token:申请令牌端点
      /oauth/confirm_access :用户确认授权提交端点
      /oauth/error:授权服务错误信息端点
      /oauth/check_token:检验token
      /oauth/token_key:提供公有秘钥端点-JWT令牌使用	  
   8.oauth配置三点：
    继承AuthorizationServerConfigurerAdapter
    客户端详情配置-哪些客户端可以到授权服务器来申请令牌-数据库获取 
    端点和令牌服务的配置-令牌的产生方式
    端点完全策略的配置	
   9.oauth用户信息的自动认证 实现这个UserDetailsService接口。spring security 会调用这个接口，完成当前用户信息的认证
   spring security 是单点登录模式，只是通过实现这个UserDetailsService接口校验登录的账号和密码是否正取
   oauth2.0 在  spring security 基础新增了授权码模式和token认证方式
三、登录实现--系统提供的登录
  
  校验过程：
	1.拦截器拦截，委托Provider进行用户信息校验（两种模式）
    2.AbstractUserDetailsAuthenticationProvider.authenticate()子类DaoAuthenticationProvider:
     对当前登录用户进行信息校验，并且将登录信息设置到Authentication（UsernamePasswordAuthenticationToken）
	3.UserDetailsServiceImpl.loadUserByUsername(String username)的作用：
      授权码模式:发起认证请求时进行客户端id和秘钥的认证
	  密码模式：获取token时会请求这个接口两次，第一次是验证客户端id和秘钥是否与内存配置的一致。
	            第二次是验证输入的用户名和密码是否与数据库的用户信息一致--所有自定义登录接口的时候不需要在验证密码的正确性
  oath提供的认证服务实现：
   密码模式:
    1.自定义登录接口
	2.根据用户名和密码以及客户端id、密钥生成令牌(自动完成用户认证)。访问oauth/token接口--校验客户端是否正确、校验用户信息是否正取 、生成token-oauth自动生成
	3.将token设置到Authentication
	4.登录成功
	
	资源服务器：
    ResourceServerConfigurerAdapter：微服务授权配置类：通过公钥校验请求令牌是否合法，完成授权
	1.configure（）；配置JWT令牌的检验信息
	2.配置放行和检验的地址
	3.需要把认证服生成JWT令牌的配置tokenStore拿过来
四、登录实现-自定义登录-不适用oauth提供的接口，自己生成秘钥
    1.自定义登录接口
	2.判断用户是否正确
	3.生成JWT令牌-载荷、头部、签名
	3.将token设置到cookie中
	4.登录成功
	资源服务：
	自定义拦截器
	获取JWT令牌
	根据私钥对JWT令牌进行解密，解密
	解密成功放行
