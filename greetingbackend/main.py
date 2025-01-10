def greet_user(request):
    username = request.args.get('username', 'Guest')
    if username == 'admin':
        return f"You are the admin. Welcome back!"
    else:
        return f"Welcome {username}"